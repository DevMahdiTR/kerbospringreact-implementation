package org.example.authentication.controller;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.example.authentication.dto.auth.LogInDTO;
import org.example.authentication.dto.auth.RegisterDTO;
import org.example.authentication.dto.auth.RegisterResponseDTO;
import org.example.authentication.security.utility.SecurityConstants;
import org.example.authentication.service.auth.AuthService;
import org.example.authentication.utility.APIRouters;
import org.example.authentication.utility.CustomerResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping(APIRouters.AUTH_ROUTER)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public CustomerResponse<RegisterResponseDTO> register(@NotNull @Valid @RequestBody final RegisterDTO registerDTO) throws Exception {
        return authService.register(registerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> logIn(@NotNull @Valid @RequestBody final LogInDTO logInDTO) throws Exception {
        return authService.logIn(logInDTO);
    }

    @GetMapping("/validate/{token}")
    public CustomerResponse<String> validateToken(@PathVariable("token") @NotNull final String token){
        return authService.validateToken(token);
    }

    @GetMapping("/confirm")
    public String activateAccount(@RequestParam("token")final String confirmationToken) {
        return authService.activateAccount(confirmationToken);
    }

    @GetMapping("/renew")
    public ResponseEntity<Object> renewToken(
            @RequestParam(name = "token") @NotNull String token,
            @RequestParam(name = "userId") final UUID userId) throws Exception{
        return authService.renewToken(token, userId);
    }

    @GetMapping("/decrypt")
    public static JWTClaimsSet decryptAndExtractClaims(@RequestParam(name ="token") String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(new RSADecrypter(SecurityConstants.ENCRYPTION_KEY.toRSAPrivateKey()));
            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();
            if (!signedJWT.verify(new RSASSAVerifier(SecurityConstants.SIGNING_KEY.toRSAPublicKey()))) {
                throw new RuntimeException("JWT signature verification failed");
            }
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/decryptSessionKey")
    public String decryptSessionKey(
            @RequestParam(name = "passwordHash") String passwordHash,
            @RequestParam(name = "ciphertext") String ciphertext
    ) throws Exception {
        return authService.decryptSessionKey(passwordHash.replace(" ","+"), ciphertext.replace(" ", "+"));
    }


}
