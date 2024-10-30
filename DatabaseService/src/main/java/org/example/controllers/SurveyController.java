package org.example.controllers;

import java.util.UUID;
import org.example.DbService;
import org.example.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SurveyController {

    private final DbService dbService;

    @Autowired
    public SurveyController(DbService dbService) {
        this.dbService = dbService;
    }

    @PostMapping("/user/{email}/survey")
    public String postNewSurvey(@PathVariable("email") String email,
                               @RequestBody String body) {
        var id = dbService.addSurvey(email, body);
        return id.toString();
    }

    @PostMapping("/survey/{survey_id}/access")
    public void addSurveyAccess(@PathVariable("survey_id") UUID surveyId,
                                @RequestBody AccessData accessData) {
        dbService.addSurveyAccess(surveyId, accessData);
    }

    @GetMapping("/survey/{survey_id}/access")
    public AccessData getSurveyAccess(@PathVariable("survey_id") UUID surveyId) {
        return dbService.getSurveyAccess(surveyId);
    }

    @GetMapping("/survey/{survey_id}")
    public String getSurvey(@PathVariable("survey_id") UUID surveyId) {
        var surveyBody = dbService.getSurvey(surveyId);
        return surveyBody.getSurvey();
    }

    @DeleteMapping("/survey/{survey_id}")
    public void deleteSurvey(@PathVariable("survey_id") UUID surveyId) {
        dbService.deleteSurvey(surveyId);
    }

    @PatchMapping("/survey/{survey_id}")
    public void updateSurvey(@PathVariable("survey_id") UUID surveyId,
                             @RequestBody String body) {
        dbService.updateSurvey(surveyId, body);
    }

    @GetMapping("/user/{email}/surveys")
    public SurveyWithId[] getSurveys(@PathVariable("email") String email) {
        var surveysBody = dbService.getSurveys(email);
        return surveysBody.stream()
                .map(val -> new SurveyWithId(val.getId(), val.getSurvey()))
                .toArray(SurveyWithId[]::new);
    }

    @PostMapping("/user/{email}/survey/{survey_id}/answer")
    public String addAnswerOnSurvey(@PathVariable("email") String email,
                                    @PathVariable("survey_id") UUID surveyId,
                                    @RequestBody String body) {
        var id = dbService.addResponseOnSurvey(email, surveyId, body);
        return id.toString();
    }
}

