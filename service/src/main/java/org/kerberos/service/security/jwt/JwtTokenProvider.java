package org.kerberos.service.security.jwt;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.kerberos.service.exceptions.custom.InvalidTokenException;
import org.kerberos.service.security.utility.CustomUserDetailsService;
import org.kerberos.service.security.utility.SecurityConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;


@Component
public class JwtTokenProvider {

    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    public String generateTokenFromTicket(JWTClaimsSet ticket) throws JOSEException {


        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(SecurityConstants.SIGNING_KEY.getKeyID()).build(),
                ticket);
        signedJWT.sign(new RSASSASigner(SecurityConstants.SIGNING_KEY));

        JWEObject jweObject = new JWEObject(
                new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                        .contentType("JWT")
                        .build(),
                new Payload(signedJWT));
        jweObject.encrypt(new RSAEncrypter(SecurityConstants.ENCRYPTION_KEY));
        return jweObject.serialize();
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
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(token);
        System.out.println("UserDetails: " + userDetails);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public <T> T extractClaim(String token, @NotNull Function<JWTClaimsSet, T> claimResolver) {
        final var claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public JWTClaimsSet extractAllClaims(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(new RSADecrypter(SecurityConstants.BLACK_KEY.toRSAPrivateKey()));
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
