package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class FrontController {

    private final DbService dbService;

    @Autowired
    public FrontController(DbService dbService) {
        this.dbService = dbService;
    }

    @PostMapping("/user/{email}")
    public String getNewUser(@PathVariable("email") String email) {
        var id = dbService.addUser(email);
        return id;
    }

    @PostMapping("/user/{email}/survey")
    public String getNewSurvey(@PathVariable("email") String email,
                               @RequestBody String body) {
        var id = dbService.addSurvey(email, body);
        return id.toString();
    }

    @GetMapping("/user/{email}/survey/{survey_id}")
    public String returnSurvey(@PathVariable("email") String email,
                               @PathVariable("survey_id") Long surveyId) {
        var surveyBody = dbService.getSurvey(email, surveyId);
        return surveyBody;
    }

    @GetMapping("/user/{email}/surveys")
    public String[] returnSurveys(@PathVariable("email") String email) {
        var surveysBody = dbService.getSurveys(email);
        return surveysBody;
    }

    @PostMapping("/user/{email}/survey/{survey_id}/answer")
    public String getAnswerOnSurvey(@PathVariable("email") String email,
                                    @PathVariable("survey_id") Long surveyId,
                                    @RequestBody String body) {
        var id = dbService.addResponseOnSurvey(email, surveyId, body);
        return id.toString();
    }
}