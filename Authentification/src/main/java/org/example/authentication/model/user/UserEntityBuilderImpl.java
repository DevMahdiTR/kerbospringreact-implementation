package org.example.authentication.model.user;

import org.example.authentication.model.role.Role;

import java.util.UUID;

public class UserEntityBuilderImpl implements UserEntityBuilder {

    private UUID id;
    private String email;
    private String secretKey;
    private String sessionKey;
    private String password;
    private boolean isEnabled;
    private Role userRole;

    @Override
    public UserEntityBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    @Override
    public UserEntityBuilder email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public UserEntityBuilder password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public UserEntityBuilder isEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    @Override
    public UserEntityBuilder userRole(Role role) {
        this.userRole = role;
        return this;
    }

    @Override
    public UserEntityBuilder secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    @Override
    public UserEntityBuilder sessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
        return this;
    }

    @Override
    public UserEntity build() {
        return new UserEntity(id, email, password, isEnabled, secretKey,sessionKey,userRole);
    }
}
