package org.kerberos.service.service.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import org.kerberos.service.dto.Authenticator;
import org.kerberos.service.dto.LogInResponseDTO;
import org.kerberos.service.security.jwt.JwtTokenProvider;
import org.kerberos.service.security.utility.CryptoUtil;
import org.kerberos.service.security.utility.SecurityConstants;
import org.kerberos.service.utility.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final CryptoUtil cryptoUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(CryptoUtil cryptoUtil, JwtTokenProvider jwtTokenProvider) {
        this.cryptoUtil = cryptoUtil;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public CustomerResponse<LogInResponseDTO> login(String encryptedTgt, String encryptedAuthenticator) throws Exception {

        final JWTClaimsSet ticket = cryptoUtil.decryptTicket(encryptedTgt, SecurityConstants.BLACK_KEY);
        System.out.println("Ticket: " + ticket);

        final String sessionKey = ticket.getClaim("session key").toString();

        final Authenticator authenticator = new ObjectMapper().readValue(cryptoUtil.decrypt(encryptedAuthenticator,sessionKey), Authenticator.class);

        System.out.println("Authenticator: " + authenticator);
        final String token = jwtTokenProvider.generateTokenFromTicket(ticket);

        final LogInResponseDTO logInResponse = LogInResponseDTO.builder().token(token).build();
        return new CustomerResponse<>(logInResponse, HttpStatus.OK);
    }

    @Override
    public CustomerResponse<String> validateToken(String token) throws Exception {
        final  Boolean jwtClaimsSet = jwtTokenProvider.validateToken(token);
        return new CustomerResponse<>("Token is Valid", HttpStatus.OK);
    }
}
