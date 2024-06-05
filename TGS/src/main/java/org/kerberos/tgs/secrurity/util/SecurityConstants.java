package org.kerberos.tgs.secrurity.util;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.JSONObjectUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;

public final class SecurityConstants {

    private SecurityConstants() {}

    public static final String SECRET_IV = "NWYyZDJlMmMyZTJkMmUyZg==";
    public static final String SECRET_KEY = "5f2d2e2c2e2d2e2f";
    public static final RSAKey SIGNING_KEY;
    public static final RSAKey ENCRYPTION_KEY;
    public static final RSAKey BLACK_KEY;

    static {
        try {
            ENCRYPTION_KEY = loadKey("encryptionKey.jwk");
            SIGNING_KEY = loadKey("signingKey.jwk");
            BLACK_KEY = loadKey("blackKey.jwk");
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
