package com.example.demo.dto;

import java.util.List;

public record SimpleReportData(SurveyDto surveyInfo, List<AnswersDto> answers) {
}
