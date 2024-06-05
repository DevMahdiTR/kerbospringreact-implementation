package org.kerberos.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogInResponseDTO {
    private String token;
}
