package com.example.demo.controllers;

import com.example.demo.DbService;
import com.example.demo.dto.AccessData;
import com.example.demo.dto.AcсessDataResponce;
import com.example.demo.dto.SurveyWithId;
import java.util.UUID;
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

    @PostMapping("/user/{email}/survey")
    public String addNewSurvey(@PathVariable("email") String email,
                               @RequestBody String body) {
        var id = dbService.addSurvey(email, body);
        return id.toString();
    }

    @GetMapping("/user/{email}/survey/{survey_id}")
    public String returnSurvey(@PathVariable("email") String email,
                               @PathVariable("survey_id") UUID surveyId) {
        var surveyBody = dbService.getSurvey(surveyId);
        return surveyBody.getSurvey();
    }

    @PostMapping(value = "/user/{email}/survey/{survey_id}/access")
    public void addSurveyAccess(@PathVariable("survey_id") UUID surveyId,
                                @RequestBody AccessData accessData) {
        dbService.addSurveyAccess(surveyId, accessData);
    }

    @GetMapping("/user/{email}/survey/{survey_id}/access")
    public AcсessDataResponce getSurveyAccess(@PathVariable("survey_id") UUID surveyId) {
        return dbService.getSurveyAccess(surveyId);
    }


    @DeleteMapping("/user/{email}/survey/{survey_id}")
    public void deleteSurvey(@PathVariable("survey_id") UUID surveyId) {
        dbService.deleteSurvey(surveyId);
    }

    @PatchMapping("/user/{email}/survey/{survey_id}")
    public void updateSurvey(@PathVariable("survey_id") UUID surveyId,
                             @RequestBody String body) {
        dbService.updateSurvey(surveyId, body);
    }

    @GetMapping("/user/{email}/surveys")
    public SurveyWithId[] returnSurveys(@PathVariable("email") String email) {
        var surveys = dbService.getSurveys(email);
        var surveysWithId = surveys.stream()
                .map(val -> new SurveyWithId(val.getId(), val.getSurvey()))
                .toArray(SurveyWithId[]::new);
        return surveysWithId;
    }

    @PostMapping("/user/{email}/survey/{survey_id}/answer")
    public String getAnswerOnSurvey(@PathVariable("email") String email,
                                    @PathVariable("survey_id") UUID surveyId,
                                    @RequestBody String body) {
        var id = dbService.addResponseOnSurvey(email, surveyId, body);
        return id.toString();
    }
}
