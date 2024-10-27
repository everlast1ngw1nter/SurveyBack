package com.example.demo;

import com.example.demo.dto.SurveyWithId;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class FrontController {

    private final DbClient dbClient;

    @Autowired
    public FrontController(DbClient dbClient) {
        this.dbClient = dbClient;
    }

    @PostMapping("/user/{email}")
    public String addNewUser(@PathVariable("email") String email) {
        var id = dbClient.addUser(email);
        return id;
    }

    @PostMapping("/user/{email}/survey")
    public String addNewSurvey(@PathVariable("email") String email,
                               @RequestBody String body) {
        var id = dbClient.addSurvey(email, body);
        return id.toString();
    }

    @GetMapping("/user/{email}/survey/{survey_id}")
    public String returnSurvey(@PathVariable("email") String email,
                               @PathVariable("survey_id") UUID surveyId) {
        var surveyBody = dbClient.getSurvey(surveyId);
        return surveyBody;
    }

    @DeleteMapping("/survey/{survey_id}")
    public void deleteSurvey(@PathVariable("survey_id") UUID surveyId) {
        dbClient.deleteSurvey(surveyId);
    }

    @PatchMapping("/survey/{survey_id}")
    public void updateSurvey(@PathVariable("survey_id") UUID surveyId,
                             @RequestBody String body) {
        dbClient.updateSurvey(surveyId, body);
    }

    @GetMapping("/user/{email}/surveys")
    public SurveyWithId[] returnSurveys(@PathVariable("email") String email) {
        var surveysBody = dbClient.getSurveysWithId(email);
        return surveysBody;
    }

    @PostMapping("/user/{email}/survey/{survey_id}/answer")
    public String getAnswerOnSurvey(@PathVariable("email") String email,
                                    @PathVariable("survey_id") UUID surveyId,
                                    @RequestBody String body) {
        var id = dbClient.addResponse(email, surveyId, body);
        return id.toString();
    }
}
