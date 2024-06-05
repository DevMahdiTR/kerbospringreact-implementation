package org.example.authentication.model.token.confirmation;

import com.nimbusds.jose.JOSEException;
import org.example.authentication.model.token.TokenFactory;
import org.example.authentication.model.token.TokenGenerator;
import org.example.authentication.model.user.UserEntity;

public class ConfirmationTokenFactory extends TokenFactory<ConfirmationToken> {

    @Override
    protected ConfirmationToken create(UserEntity userEntity) throws JOSEException {
        return ConfirmationToken
                .builder()
                .token(getTokenGenerator().generateToken(userEntity))
                .expired(false)
                .revoked(false)
                .userEntity(userEntity)
                .build();
    }

    @Override
    protected TokenGenerator getTokenGenerator() {
        return new ConfirmationTokenGenerator();
    }
}
