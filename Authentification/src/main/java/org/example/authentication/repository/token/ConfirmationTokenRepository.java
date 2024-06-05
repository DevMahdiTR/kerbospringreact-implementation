package org.example.authentication.repository.token;

import org.example.authentication.model.token.confirmation.ConfirmationToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends TokenRepository<ConfirmationToken> {


}
