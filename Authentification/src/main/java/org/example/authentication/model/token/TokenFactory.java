package org.example.authentication.model.token;

import com.nimbusds.jose.JOSEException;
import org.example.authentication.model.user.UserEntity;

public abstract class TokenFactory<T extends Token> {

    protected abstract T create(UserEntity userEntity) throws JOSEException;

    protected abstract TokenGenerator getTokenGenerator();

    public T build(UserEntity userEntity) throws JOSEException {
        return create(userEntity);
    }
}
