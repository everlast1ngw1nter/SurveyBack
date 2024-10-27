package com.example.demo;

import com.example.demo.dto.SurveyWithId;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DbClient {

    private final WebClient dbWebClient;


    public DbClient(WebClient dbWebClient) {
        this.dbWebClient = dbWebClient;
    }

    public String addUser(String email) {
        return dbWebClient
                .post()
                .uri("/user/{email}", email)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String addSurvey(String email, String body) {
        return dbWebClient
                .post()
                .uri("/user/{email}/survey", email)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getSurvey(UUID id) {
        return dbWebClient
                .get()
                .uri("/survey/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void deleteSurvey(UUID id) {
        dbWebClient
                .delete()
                .uri("/survey/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void updateSurvey(UUID id, String body) {
        dbWebClient
                .patch()
                .uri("/survey/{id}", id)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public String addResponse(String email, UUID surveyId, String body) {
        return dbWebClient
                .post()
                .uri("/user/{email}/survey/{survey_id}/answer", email, surveyId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public SurveyWithId[] getSurveysWithId(String email) {
        return Objects.requireNonNull(dbWebClient
                        .get()
                        .uri("/user/{email}/surveys", email)
                        .retrieve()
                        .bodyToFlux(SurveyWithId.class)
                        .collectList()
                        .block())
                .toArray(SurveyWithId[]::new);
    }
}
