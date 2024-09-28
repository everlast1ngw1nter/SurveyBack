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
        return "1";
    }

    @GetMapping("/user/{email}/survey/{survey_id}")
    public String returnSurvey(@PathVariable("email") String email,
                               @PathVariable("survey_id") String survey_id) {
        return "1";
    }

    @PostMapping("/user/{email}/survey/{survey_id}/answer")
    public String getAnswerOnSurvey(@PathVariable("email") String email,
                                    @PathVariable("survey_id") String survey_id,
                                    @RequestBody String body) {
        return "1";
    }
}
