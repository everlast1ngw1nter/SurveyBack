package com.example.demo;

import java.awt.dnd.DnDConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        var id = dbService.addNewSurvey(email, body);
        return id;
    }

    @GetMapping("/user/{email}/survey/{survey_id}")
    public String returnSurvey(@PathVariable("email") String email,
                               @PathVariable("survey_id") String survey_id) {
        var surveyBody = dbService.getSurvey(email, survey_id);
        return surveyBody;
    }

    @PostMapping("/user/{email}/survey/{survey_id}/answer")
    public String getAnswerOnSurvey(@PathVariable("email") String email,
                                    @PathVariable("survey_id") String survey_id,
                                    @RequestBody String body) {
        var id = dbService.addResponseOnSurvey(email, survey_id, body);
        return id;
    }
}
