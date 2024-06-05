package org.example.authentication.dto.user;

import org.example.authentication.model.user.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserEntityDTOMapper implements Function<UserEntity, UserEntityDTO> {
    @Override
    public UserEntityDTO apply(@NotNull UserEntity userEntity) {
        return new UserEntityDTO(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getRole(),
                userEntity.isEnabled()
        );
    }
}
