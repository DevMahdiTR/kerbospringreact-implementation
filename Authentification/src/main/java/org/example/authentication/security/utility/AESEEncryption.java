package org.example.authentication.security.utility;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AESEEncryption {
    public  String encrypt(String plaintext, String secret) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Generate a random IV
        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);

        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());

        // Prepend the IV to the encrypted message
        byte[] combined = new byte[ivBytes.length + encrypted.length];
        System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
        System.arraycopy(encrypted, 0, combined, ivBytes.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public  String decrypt(String cipherText, String secret) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Extract the IV from the encrypted message
        byte[] combined = Base64.getDecoder().decode(cipherText);
        byte[] ivBytes = new byte[16];
        System.arraycopy(combined, 0, ivBytes, 0, ivBytes.length);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        // Decrypt the message (excluding the IV)
        byte[] encrypted = new byte[combined.length - ivBytes.length];
        System.arraycopy(combined, ivBytes.length, encrypted, 0, encrypted.length);

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

    public static void main(String[] args) throws Exception {
        String secret = "4abdbf46-5d3b-414abdbf46-5d3b-41";
        String plaintext = "Mahdi";

        String encrypted = new AESEEncryption().encrypt(plaintext, secret);
        System.out.println("Encrypted: " + encrypted);

        String decrypted = new AESEEncryption().decrypt(encrypted, secret);
        System.out.println("Decrypted: " + decrypted);
    }
}
