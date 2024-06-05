package org.example.authentication.repository.token;


import org.example.authentication.model.token.access.AccessToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AccessTokenRepository extends TokenRepository<AccessToken> {

}
