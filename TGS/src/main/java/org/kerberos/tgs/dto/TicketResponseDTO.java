package org.kerberos.tgs.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponseDTO {

    private String serviceTicket;
    private String sessionKey;
}
