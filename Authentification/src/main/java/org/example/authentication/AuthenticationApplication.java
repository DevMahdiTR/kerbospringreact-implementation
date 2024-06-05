package org.example.authentication;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.authentication.model.role.Role;
import org.example.authentication.repository.RoleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@SpringBootApplication
@RequiredArgsConstructor
public class  AuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }


    private final RoleRepository roleRepository;
    @PostConstruct
    private void init(){
        roleRepository.save(new Role("CLIENT"));
        roleRepository.save(new Role("ADMIN"));
//        try {
//            RSAKey signingKey = new RSAKeyGenerator(2048).generate();
//            RSAKey encryptionKey = new RSAKeyGenerator(2048).generate();
//
//            saveKey(signingKey, "signingKey.jwk");
//            saveKey(encryptionKey, "encryptionKey.jwk");
//
//
//            System.out.println("Keys generated and saved successfully.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private static void saveKey(RSAKey key, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(key.toJSONString());
        }
    }
}
