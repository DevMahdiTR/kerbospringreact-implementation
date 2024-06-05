package org.kerberos.service.security.jwt.handler;


import org.kerberos.service.security.jwt.JwtTokenProvider;

public class TokenValidationHandler extends FilterChainHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenValidationHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean handle(final String jwtToken) {
        if (jwtToken == null || !jwtTokenProvider.validateToken(jwtToken)) {
            return false;
        }


        return handleNext(jwtToken);
    }
}
