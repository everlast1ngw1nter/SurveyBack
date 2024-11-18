package com.example.demo.controllers;

import com.example.demo.DbService;
import com.example.demo.dto.StatisticOnQuestionDto;
import com.example.demo.dto.SurveyWithId;
import com.example.demo.services.CheckingAvailabilityService;
import com.example.demo.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class StatisticController {
    private final StatisticService statisticService;

    private final CheckingAvailabilityService checkingAvailabilityService;

    @Autowired
    public StatisticController(StatisticService statisticService, CheckingAvailabilityService checkingAvailabilityService) {
        this.statisticService = statisticService;
        this.checkingAvailabilityService = checkingAvailabilityService;
    }

    @GetMapping("/survey/{survey_id}/statistic")
    public ResponseEntity<StatisticOnQuestionDto[]> returnSurvey(@PathVariable("survey_id") UUID surveyId) {
        if (!checkingAvailabilityService.isAvailableSurvey(surveyId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        var surveyStatistic = statisticService.getStatisticOfSurvey(surveyId);
        return new ResponseEntity<>(surveyStatistic, HttpStatus.OK);
    }

}
