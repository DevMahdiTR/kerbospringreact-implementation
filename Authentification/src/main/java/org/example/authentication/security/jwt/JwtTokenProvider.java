package org.example.authentication.security.jwt;


import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.example.authentication.exceptions.custom.InvalidTokenException;
import org.example.authentication.security.utility.CustomUserDetailsService;
import org.example.authentication.security.utility.SecurityConstants;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtTokenProvider {

    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    public static @NotNull Key getSignInKey(String secretCode) {
        byte[] keyBytes = Decoders.BASE64.decode(secretCode);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(new RSADecrypter(SecurityConstants.ENCRYPTION_KEY.toRSAPrivateKey()));
            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();
            if (!signedJWT.verify(new RSASSAVerifier(SecurityConstants.SIGNING_KEY.toRSAPublicKey()))) {
                throw new RuntimeException("JWT signature verification failed");
            }
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiration == null) {
                throw new InvalidTokenException("Expiration claim missing in JWT token");
            }
            boolean isExpired = expiration.before(new Date());
            return !isExpired;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(extractEmailFromJwt(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public <T> T extractClaim(String token, @NotNull Function<JWTClaimsSet, T> claimResolver) {
        final var claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public JWTClaimsSet extractAllClaims(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(new RSADecrypter(SecurityConstants.ENCRYPTION_KEY.toRSAPrivateKey()));
            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();
            if (!signedJWT.verify(new RSASSAVerifier(SecurityConstants.SIGNING_KEY.toRSAPublicKey()))) {
                throw new RuntimeException("JWT signature verification failed");
            }
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String extractEmailFromJwt(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }

}
