package org.example.authentication.model.token;

import com.nimbusds.jose.JOSEException;
import org.example.authentication.model.user.UserEntity;

public interface TokenGenerator {
    String generateToken(UserEntity userEntity) throws JOSEException;
}
