package org.example.authentication.model.user;

import org.example.authentication.model.role.Role;

import java.util.UUID;

public interface UserEntityBuilder {

    UserEntityBuilder id(final UUID id);

    UserEntityBuilder email(final String email);

    UserEntityBuilder password(final String password);

    UserEntityBuilder isEnabled(final Boolean isEnabled);

    UserEntityBuilder userRole(Role role);

    UserEntityBuilder secretKey(final String secretKey);

    UserEntityBuilder sessionKey(final String sessionKey);

    UserEntity build();
}
