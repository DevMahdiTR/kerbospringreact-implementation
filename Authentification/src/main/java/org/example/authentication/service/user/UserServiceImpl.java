package org.example.authentication.service.user;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.dto.user.UserEntityDTO;
import org.example.authentication.dto.user.UserEntityDTOMapper;
import org.example.authentication.exceptions.custom.DatabaseException;
import org.example.authentication.exceptions.custom.ResourceNotFoundException;
import org.example.authentication.model.user.UserEntity;
import org.example.authentication.repository.UserEntityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;


@Service
@Slf4j
public class UserServiceImpl implements UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final UserEntityDTOMapper userEntityDTOMapper;

    public UserServiceImpl(UserEntityRepository userEntityRepository, UserEntityDTOMapper userEntityDTOMapper) {
        this.userEntityRepository = userEntityRepository;
        this.userEntityDTOMapper = userEntityDTOMapper;
    }

    @Override
    public UserEntityDTO mapper(UserEntity userEntity) {
        return userEntityDTOMapper.apply(userEntity);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        try {
            log.info("Database Request to save User: {}", userEntity);
            final UserEntity savedUser = userEntityRepository.save(userEntity);
            log.info("{} saved successfully", savedUser);
            return savedUser;
        } catch (Exception e) {
            log.error("Error saving User: {}", e.getMessage());
            throw new DatabaseException("Error executing database query.");
        }
    }

    @Override
    public UserEntity saveAndFlush(UserEntity userEntity) {
        try {
            log.info("Database Request to save User: {}", userEntity);
            final UserEntity savedUser = userEntityRepository.saveAndFlush(userEntity);
            log.info("{} saved successfully", savedUser);
            return savedUser;
        } catch (Exception e) {
            log.error("Error saving User: {}", e.getMessage());
            throw new DatabaseException("Error executing database query.");
        }
    }

    public UserEntity getUserByUUID(final UUID userId) {
        return userEntityRepository.fetchUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("The User with ID %s could not be found in our system.".formatted(userId))
        );
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userEntityRepository.fetchUserByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("The User with EMAIL %s could not be found in our system.".formatted(email))
        );
    }


    @Override
    public Boolean isEmailRegistered(final String email) {
        return userEntityRepository.isEmailRegistered(email);
    }



}
