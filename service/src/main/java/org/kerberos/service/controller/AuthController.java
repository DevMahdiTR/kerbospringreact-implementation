package org.kerberos.service.controller;

import org.kerberos.service.dto.LogInResponseDTO;
import org.kerberos.service.service.auth.AuthService;
import org.kerberos.service.utility.CustomerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public CustomerResponse<LogInResponseDTO> login(
            @RequestParam(name = "encryptedTgt") String encryptedTgt,
            @RequestParam(name = "encryptedAuthenticator") String encryptedAuthenticator
    ) throws Exception {
        return authService.login(encryptedTgt, encryptedAuthenticator.replace(" ", "+"));
    }

    @GetMapping("/validate-token")
    public CustomerResponse<String> validateToken(
            @RequestParam(name = "token") String token
    ) throws Exception {
        return authService.validateToken(token);
    }
}
