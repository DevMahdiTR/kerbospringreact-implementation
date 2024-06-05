package org.kerberos.tgs.service.auth;

import org.kerberos.tgs.dto.TicketResponseDTO;
import org.kerberos.tgs.model.Authenticator;
import org.kerberos.tgs.utility.CustomerResponse;

public interface AuthService {
    CustomerResponse<TicketResponseDTO> generateServiceTicket(final String tgt, final String serviceId, final String authenticator) throws Exception;
}
