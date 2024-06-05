package org.kerberos.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

/*
    @PostConstruct
    private void init(){
        try {
            RSAKey encryptionKey = new RSAKeyGenerator(2048).generate();
            saveKey(encryptionKey, "encryptionKey.jwk");
            System.out.println("Keys generated and saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void saveKey(RSAKey key, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(key.toJSONString());
        }
    }
 */
}

