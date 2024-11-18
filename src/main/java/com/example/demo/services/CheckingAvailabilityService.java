package com.example.demo.services;

import com.example.demo.DbService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class CheckingAvailabilityService {
    private final DbService dbClient;

    @Autowired
    public CheckingAvailabilityService(DbService dbClient) {
        this.dbClient = dbClient;
    }

    public boolean isAvailableEmail(String email) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var emailInToken = authentication.getName();
        return Objects.equals(email, emailInToken);
    }

    public boolean isAvailableSurvey(UUID surveyId) {
        var email = dbClient.getSurvey(surveyId).getUser().getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var emailInToken = authentication.getName();
        return Objects.equals(email, emailInToken);
    }
}
