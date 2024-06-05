package org.kerberos.tgs.secrurity.ticket;

import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.kerberos.tgs.exceptions.custom.InvalidTokenException;
import org.kerberos.tgs.exceptions.custom.UnauthorizedActionException;
import org.kerberos.tgs.model.Authenticator;
import org.kerberos.tgs.secrurity.util.SecurityConstants;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class TicketProvider {

    private static final long TGT_EXPIRATION_TIME_MILLIS = 1200000000; // 2 minutes in milliseconds
    private static final long AUTHENTICATOR_EXPIRATION_TIME_MILLIS = 300000000; // 30 seconds in milliseconds

    public Boolean validateTicket(String ticket) {
        try {
            JWEObject jweObject = JWEObject.parse(ticket);
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


    public void validateUserTicketFromAuthenticator(JWTClaimsSet ticket , Authenticator authenticator){
        if(!authenticator.getUserId().toString().equals(ticket.getClaim("user id").toString())){
            throw new UnauthorizedActionException("Authenticator client ID does not match TGT subject");
        }
    }
    public void validateTgtTimestamp(Date issueTime) throws UnauthorizedActionException {
        if (isTimestampExpired(issueTime, TGT_EXPIRATION_TIME_MILLIS)) {
            throw new UnauthorizedActionException("TGT timestamp expired");
        }
    }

    public void validateAuthenticatorTimestamp(Date authenticatorTime) throws UnauthorizedActionException {
        if (isTimestampExpired(authenticatorTime, AUTHENTICATOR_EXPIRATION_TIME_MILLIS)) {
            throw new UnauthorizedActionException("Authenticator timestamp expired");
        }
    }

    private boolean isTimestampExpired(Date timestamp, long allowedDurationMillis) {
        long currentTime = System.currentTimeMillis();
        long timestampTime = timestamp.getTime();
        return (currentTime - timestampTime) > allowedDurationMillis;
    }

}
