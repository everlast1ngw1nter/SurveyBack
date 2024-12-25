package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.UserDto;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@Service
public class OAuthService {

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scopes;

    private final DbService dbService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OAuthService(DbService dbService, PasswordEncoder passwordEncoder) {
        this.dbService = dbService;
        this.passwordEncoder = passwordEncoder;
    }

    public String getUrlForTakeTokenGoogle() {
        return UriComponentsBuilder.fromUriString(authorizationUri)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scopes.replace(",", " ")) // Список в URL должен быть через пробел
                .build()
                .toUriString();
    }

    public void addInDbIfNotExisted(String email){
        var currUser = dbService.getUser(email);
        if (currUser == null) {
            currUser = new User();
            currUser.setPassword(passwordEncoder.encode(clientSecret));
            currUser.setEmail(email);
            currUser.setRole(Role.USER);
            dbService.addUser(currUser);
        }
    }

    public String getEmailUserGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> tokenRequestBody = new LinkedMultiValueMap<>();
        tokenRequestBody.add("client_id", clientId);
        tokenRequestBody.add("client_secret", clientSecret);
        tokenRequestBody.add("code", code);
        tokenRequestBody.add("redirect_uri", redirectUri);
        tokenRequestBody.add("grant_type", "authorization_code");
        // сначала обмениваем code на access_token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequestBody, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                request,
                Map.class
        );
        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
            return null;
        }
        String accessToken = (String) tokenResponse.getBody().get("access_token");
        // теперь запрашиваем эмейл
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        if (!userInfoResponse.getStatusCode().is2xxSuccessful() || userInfoResponse.getBody() == null) {
            return null;
        }
        return (String) userInfoResponse.getBody().get("email");
    }
}
