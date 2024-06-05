package org.kerberos.tgs.controller;

import org.kerberos.tgs.dto.TicketResponseDTO;
import org.kerberos.tgs.model.Authenticator;
import org.kerberos.tgs.service.auth.AuthService;
import org.kerberos.tgs.utility.CustomerResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/tgt")
@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping("/generate-service-ticket")
    public CustomerResponse<TicketResponseDTO> generateServiceTicket(
            @RequestParam(name = "tgt") final String tgt,
            @RequestParam(name = "serviceId") final String serviceId,
            @RequestParam(name = "authenticator") final String authenticator) throws Exception {
        return authService.generateServiceTicket(tgt, serviceId, authenticator.replace(" ", "+"));
    }

}
