package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SurveyDto {

    @JsonProperty("Name")
    public String name;

    @JsonProperty("Survey")
    public List<QuestionsDto> survey;

    public SurveyDto() {

    }
}
