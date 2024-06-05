package org.example.authentication.security.utility;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.JSONObjectUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;

public final class SecurityConstants {
    private SecurityConstants() {}


    public static final String SECRET_KEY = "93e164492b2c616f1e96dd76bed60040";

    public static final long CONFIRMATION_JWT_EXPIRATION = 31536000000L;
    public static final String JWT_CONFIRMATION_SECRET = "4f366309a07af2ab0de0f37db1f90ff7a1306193b1af5c44ad19df9ac3fd6f1cb78ed610a60bfc6a78f976c4a82517cb5a2b8f010367611bcb9cb6b08272cde3b29bfd32ec65a49cb99b849f71ae0ca002c30cbae4b599b0c7943c112341ccafb35520a0920fa8e4f14a23c20fa2ff05c27f7c99ab6c495099142489e732c0adbc35e529098222a5091f9562fb706665ec00bf872d9b77017bc0bbbbfa5cb4f810000cbf38816939ce0941d404769d0b7ba7cd28024f87afa540fd3beaea3c9f83a9502b92836dc24d6551fb0118244bc5d7297998247e22258812dc884c77c3a88b56d425a193919a2c782c0a6b4702f886ed9aedc94afe525df3f7732aef65";

    public static final RSAKey SIGNING_KEY;
    public static final RSAKey ENCRYPTION_KEY;
    public static final String RED_ENCRYPTION_KEY = "4591729d383934aca90bfb161ef2d";

    static {
        try {
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
