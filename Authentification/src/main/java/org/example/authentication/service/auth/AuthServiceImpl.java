package org.example.authentication.service.auth;

import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import org.example.authentication.dto.auth.LogInDTO;
import org.example.authentication.dto.auth.LogInResponseDTO;
import org.example.authentication.dto.auth.RegisterDTO;
import org.example.authentication.dto.auth.RegisterResponseDTO;
import org.example.authentication.exceptions.custom.EmailRegisteredException;
import org.example.authentication.exceptions.custom.InvalidTokenException;
import org.example.authentication.model.role.Role;
import org.example.authentication.model.token.access.AccessToken;
import org.example.authentication.model.token.access.AccessTokenFactory;
import org.example.authentication.model.token.confirmation.ConfirmationToken;
import org.example.authentication.model.token.confirmation.ConfirmationTokenFactory;
import org.example.authentication.model.user.UserEntity;
import org.example.authentication.security.jwt.JwtTokenProvider;
import org.example.authentication.security.utility.AESEEncryption;
import org.example.authentication.security.utility.CryptoUtil;
import org.example.authentication.security.utility.SecurityConstants;
import org.example.authentication.service.email.EmailService;
import org.example.authentication.service.role.RoleService;
import org.example.authentication.service.token.TokenService;
import org.example.authentication.service.user.UserEntityService;
import org.example.authentication.utility.APIRouters;
import org.example.authentication.utility.CustomerResponse;
import org.example.authentication.utility.ResponseHandler;
import org.example.authentication.utility.ThymeleafUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@Service
@AllArgsConstructor  // Constructor injection for dependencies
public class AuthServiceImpl implements AuthService {

    // Dependency injections
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityService userEntityService;
    private final RoleService roleService;
    private final TokenService<AccessToken> accessTokenTokenService;
    private final TokenService<ConfirmationToken> confirmationTokenTokenService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CryptoUtil cryptoUtil;
    private final AESEEncryption aesEncryption;
    @Override
    public CustomerResponse<RegisterResponseDTO> register(@NotNull RegisterDTO registerDTO) throws Exception {

        // Checking if the email is already registered
        if (userEntityService.isEmailRegistered(registerDTO.getEmail())) {
            throw new EmailRegisteredException("Email already in use.");
        }

        // Fetching the role for the new user
        final Role role = roleService.fetchRoleByName("CLIENT");
        final String sessionKey = UUID.randomUUID().toString().substring(0,32);
        // Creating a new user entity
        UserEntity newUser = UserEntity
                .builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .userRole(role)
                .isEnabled(false)
                .secretKey(aesEncryption.encrypt(registerDTO.getPassword(),SecurityConstants.SECRET_KEY))
                .sessionKey(sessionKey)
                .build();

        // Saving the new user entity
        UserEntity savedUser = userEntityService.save(newUser);

        // Generating tokens for the new user
        ConfirmationToken confirmationToken = new ConfirmationTokenFactory().build(savedUser);
        confirmationTokenTokenService.saveAndFlush(confirmationToken);

        //Constructing the activation link and sending it via email
        String activationLink = APIRouters.getConfirmationURL(confirmationToken.getToken());


        emailService.sendMail(
                registerDTO.getEmail(),
                "Activating your account.",
                "email-confirmation",
                Map.of(
                        "name", registerDTO.getEmail(),
                        "link", activationLink
                )
        );


        // Building response DTO
        final RegisterResponseDTO registerResponseDTO = RegisterResponseDTO
                .builder()
                .userEntityDTO(userEntityService.mapper(savedUser))
                .confirmationToken(confirmationToken.getToken())
                .build();

        return new CustomerResponse<>(registerResponseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> logIn(@NotNull LogInDTO logInDTO) throws Exception {
        // Authenticating the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logInDTO.getEmail(),
                        logInDTO.getPassword()
                )
        );
        // Setting authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Retrieving user details
        UserEntity user = userEntityService.getUserByEmail(logInDTO.getEmail());
        user.setSessionKey(UUID.randomUUID().toString().substring(0,32));
        userEntityService.saveAndFlush(user);
        accessTokenTokenService.revokeAllUserToken(user);

        AccessToken accessToken = new AccessTokenFactory().
                build(user);
        accessTokenTokenService.save(accessToken);


        System.out.println("padded : " + padToLength(aesEncryption.decrypt(user.getSecretKey(), SecurityConstants.SECRET_KEY)));
        // Building response DTO
        final LogInResponseDTO logInResponseDTO = LogInResponseDTO
                .builder()
                .userEntityDTO(userEntityService.mapper(user))
                .tgt(accessToken.getToken())
                .sessionKey(aesEncryption.encrypt(user.getSessionKey(), padToLength(aesEncryption.decrypt(user.getSecretKey(), SecurityConstants.SECRET_KEY))))
                .build();

        return ResponseHandler.generateResponse(logInResponseDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> renewToken(@NotNull String token,final UUID userId) throws Exception {
        final AccessToken existedAccessToken = accessTokenTokenService.findByToken(token);
        final UserEntity user = existedAccessToken.getUserEntity();
        if(!user.getId().equals(userId)){
            return ResponseHandler.generateResponse("Invalid user", HttpStatus.BAD_REQUEST);
        }
        accessTokenTokenService.revokeAllUserToken(user);
        final AccessToken accessToken = new AccessTokenFactory().build(user);
        accessTokenTokenService.saveAndFlush(accessToken);
        return ResponseHandler.generateResponse(accessToken.getToken(), HttpStatus.OK);
    }

    @Override
    public CustomerResponse<String> validateToken(String token) {
        final AccessToken accessToken = accessTokenTokenService.findByToken(token);
        final boolean isValid = jwtTokenProvider.validateToken(token);
        if(!isValid){
            return new CustomerResponse<>("invalid Jwt token" , HttpStatus.BAD_REQUEST);
        }
        if(accessToken.isExpired() || accessToken.isRevoked()){
            return new CustomerResponse<>("expired Jwt token" , HttpStatus.BAD_REQUEST);
        }
        return new CustomerResponse<>("Valid Jwt token" , HttpStatus.OK);
    }


    @Override
    public String activateAccount(String confirmationToken) {

        ConfirmationToken existedConfirmationToken = confirmationTokenTokenService.findByToken(confirmationToken);

        if (existedConfirmationToken.isExpired() || existedConfirmationToken.isRevoked()) {
            throw new InvalidTokenException("Invalid Token.");
        }

        UserEntity existedUser = existedConfirmationToken.getUserEntity();

        if (existedUser.isEnabled()) {
            throw new InvalidTokenException("Already activated");
        }

        existedUser.setEnabled(true);
        userEntityService.saveAndFlush(existedUser);

        return ThymeleafUtil.processEmailTemplate("confirmation-page", Collections.emptyMap());

    }


    @Override
    public String decryptSessionKey(String password, String encryptedText) throws Exception {
        final String secretKey = cryptoUtil.encrypt(password,SecurityConstants.RED_ENCRYPTION_KEY);
        return cryptoUtil.decrypt(secretKey, encryptedText);
    }


    private String padToLength(String input) {
        if (input.length() >= 16) {
            return input.substring(0, 16);
        } else {
            return input + "b".repeat(16 - input.length());
        }
    }
}
