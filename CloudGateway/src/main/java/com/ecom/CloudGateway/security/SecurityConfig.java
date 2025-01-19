package com.ecom.CloudGateway.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                    .csrf(csrfCustomizer -> csrfCustomizer.disable())
                    .authorizeExchange(exchange -> exchange.anyExchange().authenticated())
                    .oauth2Login(Customizer.withDefaults())
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                    .build();
    }

}
