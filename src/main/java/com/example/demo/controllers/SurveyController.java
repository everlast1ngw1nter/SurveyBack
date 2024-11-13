package com.example.demo.controllers;

import com.example.demo.DbService;
import com.example.demo.dto.AccessData;
import com.example.demo.dto.AccessDataResponse;
import com.example.demo.dto.SurveyWithId;
import java.util.UUID;

import com.example.demo.dto.dtoForParseBody.AccessDataAndEmail;
import com.example.demo.dto.dtoForParseBody.AnswerOnSurveyAndEmail;
import com.example.demo.dto.dtoForParseBody.SurveyDataAndEmail;
import com.example.demo.services.CheckingAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SurveyController {

    private final DbService dbService;

    private final CheckingAvailabilityService checkingAvailabilityService;

    @Autowired
    public SurveyController(DbService dbService, CheckingAvailabilityService checkingAvailabilityService) {
        this.dbService = dbService;
        this.checkingAvailabilityService = checkingAvailabilityService;
    }

    @PostMapping("/survey")
    public ResponseEntity<String> addNewSurvey(@RequestBody SurveyDataAndEmail body) {
        if (!checkingAvailabilityService.isAvailableEmail(body.email))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        var id = dbService.addSurvey(body.email, body.surveyData);
        return new ResponseEntity<>(id.toString(), HttpStatus.OK);
    }

    @GetMapping("/survey/{survey_id}")
    public ResponseEntity<String> returnSurvey(@PathVariable("survey_id") UUID surveyId) {
        if (!checkingAvailabilityService.isAvailableSurvey(surveyId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        var surveyBody = dbService.getSurvey(surveyId);
        return new ResponseEntity<>(surveyBody.getSurvey(), HttpStatus.OK);
    }

    @PostMapping(value = "/survey/{survey_id}/access")
    public ResponseEntity<Void> addSurveyAccess(@PathVariable("survey_id") UUID surveyId,
                                @RequestBody AccessDataAndEmail accessData) {
        dbService.addSurveyAccess(surveyId, accessData.accessData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/survey/{survey_id}/access")
    public ResponseEntity<AccessDataResponse> getSurveyAccess(@PathVariable("survey_id") UUID surveyId) {
        return new ResponseEntity<>(dbService.getSurveyAccess(surveyId), HttpStatus.OK);
    }


    @DeleteMapping("/survey/{survey_id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable("survey_id") UUID surveyId) {
        if (!checkingAvailabilityService.isAvailableSurvey(surveyId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        dbService.deleteSurvey(surveyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/survey/{survey_id}")
    public ResponseEntity<Void> updateSurvey(@PathVariable("survey_id") UUID surveyId,
                             @RequestBody SurveyDataAndEmail body) {
        if (!checkingAvailabilityService.isAvailableSurvey(surveyId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        dbService.updateSurvey(surveyId, body.surveyData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/surveys")
    public ResponseEntity<SurveyWithId[]> returnSurveys(@RequestParam String email) {
        if (!checkingAvailabilityService.isAvailableEmail(email))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        var surveys = dbService.getSurveys(email);
        var surveysWithId = surveys.stream()
                .map(val -> new SurveyWithId(val.getId(), val.getSurvey()))
                .toArray(SurveyWithId[]::new);
        return new ResponseEntity<>(surveysWithId, HttpStatus.OK);
    }


    @PostMapping("/survey/{survey_id}/answer")
    public ResponseEntity<String> getAnswerOnSurvey(@PathVariable("survey_id") UUID surveyId,
                                    @RequestBody AnswerOnSurveyAndEmail body) {
        if (!checkingAvailabilityService.isAvailableSurvey(surveyId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        var id = dbService.addResponseOnSurvey(body.email, surveyId, body.answer);
        return new ResponseEntity<>(id.toString(), HttpStatus.OK);
    }
}
