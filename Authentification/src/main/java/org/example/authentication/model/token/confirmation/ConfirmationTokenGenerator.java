package org.example.authentication.model.token.confirmation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.authentication.model.token.TokenGenerator;
import org.example.authentication.model.user.UserEntity;
import org.example.authentication.security.jwt.JwtTokenProvider;
import org.example.authentication.security.utility.SecurityConstants;

import java.util.Date;

public class ConfirmationTokenGenerator implements TokenGenerator {
    @Override
    public String generateToken(UserEntity userEntity) {
        String email = userEntity.getEmail();
        Date currentData = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + SecurityConstants.CONFIRMATION_JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentData)
                .setExpiration(expireDate)
                .signWith(JwtTokenProvider.getSignInKey(SecurityConstants.JWT_CONFIRMATION_SECRET), SignatureAlgorithm.HS256)
                .compact();
    }
}
