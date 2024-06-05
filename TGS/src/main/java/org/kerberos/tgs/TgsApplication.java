package org.kerberos.tgs;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@SpringBootApplication
public class TgsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgsApplication.class, args);
	}

	@PostConstruct
	private void init(){
		/*
		try {
            RSAKey redKey = new RSAKeyGenerator(2048).generate();

            saveKey(redKey, "blackKey.jwk");
            System.out.println("Keys generated and saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
		*/
	}

	/*
	private static void saveKey(RSAKey key, String filename) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(filename);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(key.toJSONString());
		}
	}
	*/
}
