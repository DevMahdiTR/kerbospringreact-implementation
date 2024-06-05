package org.example.authentication.model.token.access;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.model.token.TokenGenerator;
import org.example.authentication.model.user.UserEntity;
import org.example.authentication.security.utility.SecurityConstants;

import java.util.Date;

@Slf4j
public class AccessTokenGenerator implements TokenGenerator {
    @Override
    public String generateToken(UserEntity userEntity) throws JOSEException {
        String email = userEntity.getEmail();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .claim("session key", userEntity.getSessionKey())
                .claim("user id", userEntity.getId())
                .claim("role", userEntity.getRole().getName())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600000))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(SecurityConstants.SIGNING_KEY.getKeyID()).build(),
                claimsSet);
        signedJWT.sign(new RSASSASigner(SecurityConstants.SIGNING_KEY));

        JWEObject jweObject = new JWEObject(
                new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                        .contentType("JWT")
                        .build(),
                new Payload(signedJWT));
        jweObject.encrypt(new RSAEncrypter(SecurityConstants.ENCRYPTION_KEY));
        return jweObject.serialize();

    }
}
