package com.example.demo.dto.dtoForParseBody;

import com.example.demo.dto.QuestionsDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class AnswerOnSurveyAndEmail {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty("email")
    public String email;

    @JsonProperty("data")
    public String answer;

    public AnswerOnSurveyAndEmail() {

    }

    public void setData(Object data) throws JsonProcessingException {
        this.answer = objectMapper.writeValueAsString(data);
    }
}
