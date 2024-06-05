package org.example.authentication.service.auth;

import com.nimbusds.jose.JOSEException;
import org.example.authentication.dto.auth.LogInDTO;
import org.example.authentication.dto.auth.RegisterDTO;
import org.example.authentication.dto.auth.RegisterResponseDTO;
import org.example.authentication.utility.CustomerResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface AuthService {

    CustomerResponse<RegisterResponseDTO> register(@NotNull final RegisterDTO registerDTO) throws Exception;
    ResponseEntity<Object> logIn(@NotNull final LogInDTO logInDTO) throws Exception;
    public ResponseEntity<Object> renewToken(@NotNull String token,final UUID userId) throws Exception;
    CustomerResponse<String> validateToken(final String token);
    String activateAccount(final String confirmationToken);


    public String decryptSessionKey(String passwordHash, String encryptedText) throws Exception;

}
