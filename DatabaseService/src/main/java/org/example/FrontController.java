package org.example;

import java.util.List;
import org.example.dto.FileInfoDto;
import org.example.dto.ResponseDto;
import org.example.models.CreatedFile;
import org.example.models.Survey;
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
    public String postNewUser(@PathVariable("email") String email) {
        var id = dbService.addUser(email);
        return id;
    }

    @PostMapping("/user/{email}/survey")
    public String postNewSurvey(@PathVariable("email") String email,
                               @RequestBody String body) {
        var id = dbService.addSurvey(email, body);
        return id.toString();
    }

    @GetMapping("/survey/{survey_id}")
    public String getSurvey(@PathVariable("survey_id") Long surveyId) {
        var surveyBody = dbService.getSurvey(surveyId);
        return surveyBody.getSurvey();
    }

    @GetMapping("/responses/{survey_id}")
    public List<ResponseDto> getResponses(@PathVariable("survey_id") Long surveyId) {
        var responses = dbService.getResponses(surveyId);
        var repsArr = responses.stream()
                .map(x -> new ResponseDto(x.getResponse()))
                .toList();
        return repsArr;
    }

    @GetMapping("/file/{survey_id}")
    public CreatedFile getCreatedFile(@PathVariable("survey_id") Long surveyId) {
        var createdFile = dbService.getCreatedFile(surveyId);
        return createdFile;
    }


    @DeleteMapping("/survey/{survey_id}")
    public void deleteSurvey(@PathVariable("survey_id") Long surveyId) {
        dbService.deleteSurvey(surveyId);
    }

    @PatchMapping("/survey/{survey_id}")
    public void updateSurvey(@PathVariable("survey_id") Long surveyId,
                             @RequestBody String body) {
        dbService.updateSurvey(surveyId, body);
    }

    @GetMapping("/user/{email}/surveys")
    public Survey[] getSurveys(@PathVariable("email") String email) {
        var surveysBody = dbService.getSurveys(email);
        return surveysBody.toArray(Survey[]::new);
    }

    @PostMapping("/user/{email}/survey/{survey_id}/answer")
    public String addAnswerOnSurvey(@PathVariable("email") String email,
                                    @PathVariable("survey_id") Long surveyId,
                                    @RequestBody String body) {
        var id = dbService.addResponseOnSurvey(email, surveyId, body);
        return id.toString();
    }

    @PostMapping("/file/{survey_id}")
    public void addCreatedFile(@PathVariable("survey_id") Long surveyId,
                               @RequestBody FileInfoDto body) {
        dbService.addCreatedFile(surveyId, body.file(), body.answersCount());
    }

    @PatchMapping("/file/{survey_id}")
    public void updateCreatedFile(@PathVariable("survey_id") Long surveyId,
                               @RequestBody FileInfoDto body) {
        dbService.updateCreatedFile(surveyId, body.file(), body.answersCount());
    }
}

