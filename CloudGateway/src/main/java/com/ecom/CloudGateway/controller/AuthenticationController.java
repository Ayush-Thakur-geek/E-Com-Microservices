package com.ecom.CloudGateway.controller;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.CloudGateway.model.AuthenticationResponse;

import ch.qos.logback.core.model.Model;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private final String client_secret = "yvkTtYcB5XpEiJlVynmMnkqWECUrIYJ3PcoLYOVP1wqfRPb5AJgULYiAXASju2J0"; 

    @GetMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
        @AuthenticationPrincipal OidcUser oidcUser,
        Model model,
        @RegisteredOAuth2AuthorizedClient("okta")
        OAuth2AuthorizedClient client
    ) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String accessToken = decodeAccessToken(client.getAccessToken().getTokenValue());
        AuthenticationResponse response 
            = AuthenticationResponse.builder()
                .userId(oidcUser.getEmail())
                .accessToken(accessToken)
                .refreshToken(client.getRefreshToken().getTokenValue())
                .expiresIn(client.getAccessToken().getExpiresAt().getEpochSecond())
                .authorityList(oidcUser.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList())
                )
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String decodeAccessToken(String Token) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // String clientSecretBase64 = "Base64_Encoded_Client_Secret"; // 48-byte secret
        byte[] clientSecret = Base64.getDecoder().decode(client_secret);

        // Example JWE token (Base64 encoded)
        String jweTokenBase64 = "Base64_Encoded_JWE_Token";
        byte[] jweToken = Base64.getDecoder().decode(jweTokenBase64);

        // Extract the JWE components (this example assumes direct encryption with AES-GCM)
        byte[] iv = new byte[12]; // 96-bit IV (for AES-GCM)
        byte[] ciphertext = new byte[jweToken.length - iv.length - 16]; // Ciphertext (remaining part after IV and tag)
        byte[] authTag = new byte[16]; // Authentication tag

        System.arraycopy(jweToken, 0, iv, 0, iv.length);
        System.arraycopy(jweToken, iv.length, ciphertext, 0, ciphertext.length);
        System.arraycopy(jweToken, iv.length + ciphertext.length, authTag, 0, authTag.length);

        // Initialize AES-GCM decryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKey key = new javax.crypto.spec.SecretKeySpec(clientSecret, "AES");

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv); // 128-bit tag size
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

        // Decrypt the ciphertext
        byte[] decrypted = cipher.doFinal(ciphertext, 0, ciphertext.length);

        // Output the decrypted payload (could be a JWT)
        String decryptedPayload = new String(decrypted, StandardCharsets.UTF_8);

        return decryptedPayload;
    }
}
// localhost:9090/authenticate/login