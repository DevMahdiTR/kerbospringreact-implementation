package org.kerberos.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/security")
public class SecurityController {

    @GetMapping()
    public String getSecurity() {
        return "Security";
    }
}
