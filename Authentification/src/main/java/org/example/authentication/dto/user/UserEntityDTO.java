package org.example.authentication.dto.user;

import org.example.authentication.model.role.Role;

import java.util.UUID;

public record UserEntityDTO(
        UUID id,
        String email,
        Role role,
        boolean isEnabled
) {
}
