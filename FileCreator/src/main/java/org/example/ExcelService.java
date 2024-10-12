package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.example.dto.AnswersDto;
import org.example.dto.QuestionAnswerDto;
import org.example.dto.SurveyDto;
import org.example.models.Response;
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

    private SurveyDto getQuestionsFromSurvey(String email, long surveyId) {
        var survey = dbService.getSurvey(email, surveyId);
        SurveyDto dto;
        try {
            dto = objectMapper.readValue(survey, SurveyDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    private List<AnswersDto> getAnswersFromResponses(String email, long surveyId) {
        var resps = dbService.getResponses(email, surveyId);
        return resps.stream()
                .map(this::getAnswersFromResponse)
                .toList();

    }

    private AnswersDto getAnswersFromResponse(Response response) {
        AnswersDto dto;
        try {
            Map<String, QuestionAnswerDto> data = objectMapper.readValue(response.getResponse(), objectMapper.getTypeFactory().constructMapType(Map.class, String.class, QuestionAnswerDto.class));
            var answers = data.values()
                    .stream()
                    .map(qaDto -> qaDto.answer)
                    .toList();
            dto = new AnswersDto(answers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    public String getExcelFile(String email, long surveyId) {
        var questions = getQuestionsFromSurvey(email, surveyId);
        var answers = getAnswersFromResponses(email, surveyId);
        return "1";
    }
}
