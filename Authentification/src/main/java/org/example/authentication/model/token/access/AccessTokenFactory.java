package org.example.authentication.model.token.access;

import com.nimbusds.jose.JOSEException;
import org.example.authentication.model.token.TokenFactory;
import org.example.authentication.model.token.TokenGenerator;
import org.example.authentication.model.user.UserEntity;

public class AccessTokenFactory extends TokenFactory<AccessToken> {


    @Override
    protected AccessToken create(UserEntity userEntity) throws JOSEException {
        return AccessToken.
                builder()
                .token(getTokenGenerator().generateToken(userEntity))
                .expired(false)
                .revoked(false)
                .userEntity(userEntity)
                .build();
    }

    @Override
    protected TokenGenerator getTokenGenerator() {
        return new AccessTokenGenerator();
    }
}
