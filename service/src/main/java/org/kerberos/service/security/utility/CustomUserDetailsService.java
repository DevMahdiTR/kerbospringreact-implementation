package org.kerberos.service.security.utility;


import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CryptoUtil cryptoUtil;

    public CustomUserDetailsService(CryptoUtil cryptoUtil) {
        this.cryptoUtil = cryptoUtil;
    }


    @Override
    public UserDetails loadUserByUsername(String ticket) throws UsernameNotFoundException {
        final JWTClaimsSet jwtClaimsSet = cryptoUtil.decryptTicket(ticket,SecurityConstants.ENCRYPTION_KEY);

        return new User(
                jwtClaimsSet.getSubject(),
                jwtClaimsSet.getClaim("session key").toString(),
                Collections.singletonList(new SimpleGrantedAuthority(jwtClaimsSet.getClaim("role").toString()))
        );

    }
}
