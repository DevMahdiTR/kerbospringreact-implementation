package org.kerberos.service.security.utility;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.JSONObjectUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;

public final class SecurityConstants {

    private SecurityConstants() {}

//    public static final String JWT_ACCESS_SECRET = "d9f797412704ca8fc0d1e03219dd510fc2e9d011548771c0b703183d0617169386073c28cc179d182e32dcde83f0dcc883ea6627d3b4d313d6d2e53b59593952";
public static final Long ACCESS_JWT_EXPIRATION = 2678400000L; // 31 days in milliseconds

    public static final RSAKey BLACK_KEY;
    public static final RSAKey SIGNING_KEY;
    public static final RSAKey ENCRYPTION_KEY;

    static {
        try {
            BLACK_KEY = loadKey("blackKey.jwk");
            SIGNING_KEY = loadKey("signingKey.jwk");
            ENCRYPTION_KEY = loadKey("encryptionKey.jwk");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load keys", e);
        }
    }

    private static RSAKey loadKey(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            String keyJson = (String) ois.readObject();
            return RSAKey.parse(JSONObjectUtils.parse(keyJson));
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to parse key", e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
