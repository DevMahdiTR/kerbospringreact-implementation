package org.kerberos.tgs.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.kerberos.tgs.dto.TicketResponseDTO;
import org.kerberos.tgs.exceptions.custom.UnauthorizedActionException;
import org.kerberos.tgs.model.Authenticator;
import org.kerberos.tgs.secrurity.ticket.TicketProvider;
import org.kerberos.tgs.secrurity.util.CryptoUtil;
import org.kerberos.tgs.utility.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final CryptoUtil cryptoUtil;
    private final ObjectMapper objectMapper ;
    private final TicketProvider ticketProvider;

    public AuthServiceImpl(CryptoUtil cryptoUtil, ObjectMapper objectMapper, TicketProvider ticketProvider) {
        this.cryptoUtil = cryptoUtil;
        this.objectMapper = objectMapper;
        this.ticketProvider = ticketProvider;
    }

    @Override
    public CustomerResponse<TicketResponseDTO> generateServiceTicket(final String tgt, final String serviceId, final String encryptedAuthenticator) throws Exception {


        // Decrypt TGT
        final JWTClaimsSet decryptedTgt = cryptoUtil.decryptTGT(tgt);
        final String sessionKey = decryptedTgt.getClaim("session key").toString();

        // Decrypt Authenticator
        final String decryptedAuthenticator = cryptoUtil.decrypt(encryptedAuthenticator, sessionKey);
        final Authenticator authenticator = objectMapper.readValue(decryptedAuthenticator, Authenticator.class);

        // Validate TGT timestamp
        ticketProvider.validateTgtTimestamp(decryptedTgt.getIssueTime());
        // Validate Authenticator timestamp
        ticketProvider.validateAuthenticatorTimestamp(authenticator.getTimestamp());


        // Verify Authenticator contents
        ticketProvider.validateUserTicketFromAuthenticator(decryptedTgt, authenticator);

        // TODO: Implement Authenticator ID validation
        // Ensure Authenticator is not reused
        // For example, store and check a unique identifier from the Authenticator

        // Generate new session key
        final String newSessionKey = UUID.randomUUID().toString().substring(0, 32);

        // Encrypt TGS Ticket and Session Key
        final String encryptedTgsTicket = cryptoUtil.encryptTGS(
                decryptedTgt.getSubject(),
                decryptedTgt.getClaim("user id").toString(),
                newSessionKey,
                decryptedTgt.getClaim("role").toString()
        );
        final String encryptedNewSessionKey = cryptoUtil.encrypt(newSessionKey, sessionKey);

        // Build Ticket Response DTO
        final TicketResponseDTO ticketResponse = TicketResponseDTO.builder()
                .serviceTicket(encryptedTgsTicket)
                .sessionKey(encryptedNewSessionKey)
                .build();

        return new CustomerResponse<>(ticketResponse, HttpStatus.OK);
    }


}
