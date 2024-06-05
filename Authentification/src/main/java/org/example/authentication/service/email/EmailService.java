package org.example.authentication.service.email;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface EmailService {


    public void sendMail(String to, String subject, String templateName, Map<String, Object> model);
}


