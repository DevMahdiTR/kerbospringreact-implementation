package org.example.authentication.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.repository.token.AccessTokenRepository;
import org.example.authentication.security.jwt.handler.FilterChainHandler;
import org.example.authentication.security.jwt.handler.TokenValidationHandler;
import org.example.authentication.security.jwt.handler.UserValidationHandler;
import org.example.authentication.security.utility.CustomUserDetailsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws IOException {

        try {
            final String jwtToken = jwtTokenProvider.resolveToken(request);
            FilterChainHandler filterChainHandler = new TokenValidationHandler(jwtTokenProvider, accessTokenRepository);
            filterChainHandler.setNextHandler(new UserValidationHandler(jwtTokenProvider, customUserDetailsService));

            if (!filterChainHandler.handle(jwtToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            var authToken = jwtTokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            log.error("Error logging in: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }
}
