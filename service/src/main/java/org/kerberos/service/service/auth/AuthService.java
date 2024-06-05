package org.kerberos.service.service.auth;

import org.kerberos.service.dto.LogInResponseDTO;
import org.kerberos.service.utility.CustomerResponse;

public interface AuthService {


    CustomerResponse<LogInResponseDTO> login(final String encryptedTgt, final String encryptedAuthenticator) throws Exception;
    CustomerResponse<String> validateToken(final String token) throws Exception;
}
