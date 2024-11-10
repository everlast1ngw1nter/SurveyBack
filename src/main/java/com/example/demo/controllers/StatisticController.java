package com.example.demo.controllers;

import com.example.demo.DbService;
import com.example.demo.dto.StatisticOnQuestionDto;
import com.example.demo.dto.SurveyWithId;
import com.example.demo.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class StatisticController {
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/user/{email}/survey-statistic/{survey_id}")
    public StatisticOnQuestionDto[] returnSurvey(@PathVariable("email") String email,
                                                 @PathVariable("survey_id") UUID surveyId) {
        var surveyStatistic = statisticService.GetStatisticOfSurvey(surveyId);
        return surveyStatistic;
    }

}
