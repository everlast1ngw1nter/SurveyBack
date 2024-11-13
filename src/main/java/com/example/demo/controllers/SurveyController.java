package com.example.demo.controllers;

import com.example.demo.DbService;
import com.example.demo.dto.AccessData;
import com.example.demo.dto.AccessDataResponse;
import com.example.demo.dto.SurveyWithId;
import java.util.UUID;

import com.example.demo.dto.dtoForParseBody.AccessDataAndEmail;
import com.example.demo.dto.dtoForParseBody.AnswerOnSurveyAndEmail;
import com.example.demo.dto.dtoForParseBody.SurveyDataAndEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SurveyController {

    private final DbService dbService;

    @Autowired
    public SurveyController(DbService dbService) {
        this.dbService = dbService;
    }

    @PostMapping("/survey")
    public String addNewSurvey(@RequestBody SurveyDataAndEmail body) {
        var id = dbService.addSurvey(body.email, body.surveyData);
        return id.toString();
    }

    @GetMapping("/survey/{survey_id}")
    public String returnSurvey(@PathVariable("survey_id") UUID surveyId) {
        var surveyBody = dbService.getSurvey(surveyId);
        return surveyBody.getSurvey();
    }

    @PostMapping(value = "/survey/{survey_id}/access")
    public void addSurveyAccess(@PathVariable("survey_id") UUID surveyId,
                                @RequestBody AccessDataAndEmail accessData) {
        dbService.addSurveyAccess(surveyId, accessData.accessData);
    }

    @GetMapping("/survey/{survey_id}/access")
    public AccessDataResponse getSurveyAccess(@PathVariable("survey_id") UUID surveyId) {
        return dbService.getSurveyAccess(surveyId);
    }


    @DeleteMapping("/survey/{survey_id}")
    public void deleteSurvey(@PathVariable("survey_id") UUID surveyId) {
        dbService.deleteSurvey(surveyId);
    }

    @PatchMapping("/survey/{survey_id}")
    public void updateSurvey(@PathVariable("survey_id") UUID surveyId,
                             @RequestBody SurveyDataAndEmail body) {
        dbService.updateSurvey(surveyId, body.surveyData);
    }

    @GetMapping("/surveys")
    public SurveyWithId[] returnSurveys(@RequestParam String email) {
        var surveys = dbService.getSurveys(email);
        var surveysWithId = surveys.stream()
                .map(val -> new SurveyWithId(val.getId(), val.getSurvey()))
                .toArray(SurveyWithId[]::new);
        return surveysWithId;
    }


    @PostMapping("/survey/{survey_id}/answer")
    public String getAnswerOnSurvey(@PathVariable("survey_id") UUID surveyId,
                                    @RequestBody AnswerOnSurveyAndEmail body) {
        var id = dbService.addResponseOnSurvey(body.email, surveyId, body.answer);
        return id.toString();
    }
}
