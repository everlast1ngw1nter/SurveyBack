package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.SurveyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

    private final DbService dbService;

    private final ObjectMapper objectMapper;

    @Autowired
    public ExcelService(DbService dbService, ObjectMapper objectMapper) {
        this.dbService = dbService;
        this.objectMapper = objectMapper;
    }

    public String generateExcelFile(String email, long surveyId) {
        var survey = dbService.getSurvey(email, surveyId);
        SurveyDto dto = null;
        try {
            dto = objectMapper.readValue(survey, SurveyDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return survey;
    }
}
