package org.example.authentication.dto.auth;

import lombok.Builder;
import lombok.Data;
import org.example.authentication.dto.user.UserEntityDTO;

@Data
@Builder
public class LogInResponseDTO {
    private UserEntityDTO userEntityDTO;
    private String tgt;
    private String sessionKey;
}
