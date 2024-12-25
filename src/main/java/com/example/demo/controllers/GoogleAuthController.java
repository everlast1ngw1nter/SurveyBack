package com.example.demo.controllers;

import com.example.demo.services.JwtGeneratorService;
import com.example.demo.services.OAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth/google")
public class GoogleAuthController {

    private final OAuthService oAuthService;

    @Autowired
    public GoogleAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping
    public void googleLogin(HttpServletResponse response) throws IOException {
        System.out.println("Z");
        response.sendRedirect(oAuthService.getUrlForTakeTokenGoogle());
    }

    @GetMapping("/callback")
    public void googleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response) throws IOException {

        var userEmail = oAuthService.getEmailUserGoogle(code);
        if (userEmail == null) {
            response.sendRedirect("http://localhost:3000/error");
        }
        oAuthService.addInDbIfNotExisted(userEmail);
        var token = JwtGeneratorService.generateToken(userEmail);
        var cookieJWT = new Cookie("Token", token);
        cookieJWT.setSecure(true);
        cookieJWT.setPath("/");
        response.addCookie(cookieJWT);

        var cookieEmail = new Cookie("Email", userEmail);
        cookieEmail.setSecure(true);
        cookieEmail.setPath("/");
        response.addCookie(cookieEmail);
        response.sendRedirect("http://localhost:3000/my-surveys");
    }
}