package com.example.demo.dto;

import java.util.List;

public class StatisticOnQuestionDto {
    public String questionId;
    public String question;
    public List<StatisticVariantDto> answers;

    public StatisticOnQuestionDto(String questionId, String question, List<StatisticVariantDto> answers) {
        this.questionId = questionId;
        this.question = question;
        this.answers = answers;
    }
}
