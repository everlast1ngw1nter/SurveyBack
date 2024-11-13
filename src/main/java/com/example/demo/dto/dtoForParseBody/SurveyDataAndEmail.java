package com.example.demo.dto.dtoForParseBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SurveyDataAndEmail {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty("email")
    public String email;

    @JsonProperty("data")
    public String surveyData;

    public SurveyDataAndEmail() {

    }

    public void setData(Object data) throws JsonProcessingException {
        this.surveyData = objectMapper.writeValueAsString(data);
    }
}

