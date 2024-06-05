package org.example.authentication.dto.auth;


import lombok.Builder;
import lombok.Data;
import org.example.authentication.dto.user.UserEntityDTO;

@Builder
@Data
public class RegisterResponseDTO {
    private UserEntityDTO userEntityDTO;
    private String confirmationToken;
}
