package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.AnswersDto;
import com.example.demo.dto.SimpleReportData;
import com.example.demo.dto.SurveyDto;
import com.example.demo.models.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswersDataService {

    private final DbService dbClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public AnswersDataService(DbService dbClient, ObjectMapper objectMapper) {
        this.dbClient = dbClient;
        this.objectMapper = objectMapper;
    }

    private SurveyDto getQuestionsFromSurvey(UUID surveyId) {
        var survey = dbClient.getSurvey(surveyId);
        SurveyDto dto;
        try {
            dto = objectMapper.readValue(survey.getSurvey(), SurveyDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    private List<AnswersDto> getAnswersFromResponses(UUID surveyId) {
        var resps = dbClient.getResponses(surveyId);
        return resps.stream()
                .map(Response::getResponse)
                .map(this::getAnswersFromResponse)
                .toList();
    }

    private AnswersDto getAnswersFromResponse(String response) {
        AnswersDto dto;
        try {
            Map<String, String> data = objectMapper.readValue(response, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
            var answers = data.values().stream().toList();
            dto = new AnswersDto(answers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    public SimpleReportData getSurveyAndAnswers(UUID surveyId) {
        var questions = getQuestionsFromSurvey(surveyId);
        var answers = getAnswersFromResponses(surveyId);
        return new SimpleReportData(questions, answers);
    }
}
