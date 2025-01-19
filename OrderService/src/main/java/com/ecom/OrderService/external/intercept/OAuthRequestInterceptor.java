package com.ecom.OrderService.external.intercept;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class OAuthRequestInterceptor implements RequestInterceptor {

    private OAuth2AuthorizedClientManager authorizedClientManager;

    public OAuthRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer "
        + authorizedClientManager
            .authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId("internal-client")
                .principal("internal")
                .build())
            .getAccessToken().getTokenValue());
    }
    

}
