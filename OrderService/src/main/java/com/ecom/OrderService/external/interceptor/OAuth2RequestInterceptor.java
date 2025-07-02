package com.ecom.OrderService.external.interceptor;

import com.ecom.OrderService.service.TokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2RequestInterceptor implements RequestInterceptor {

    private final TokenService tokenService;

    public OAuth2RequestInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = tokenService.extractToken();

        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
