package org.example;

import java.util.List;
import java.util.UUID;
import org.example.dto.*;
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

    @GetMapping("/responses/{survey_id}")
    public List<ResponseDto> getResponses(@PathVariable("survey_id") UUID surveyId) {
        var responses = dbService.getResponses(surveyId);
        var repsArr = responses.stream()
                .map(x -> new ResponseDto(x.getResponse()))
                .toList();
        return repsArr;
    }

    @GetMapping("/file/{survey_id}")
    public CreatedFileDto getCreatedFile(@PathVariable("survey_id") UUID surveyId) {
        var createdFile = dbService.getCreatedFile(surveyId);
        var dto = new CreatedFileDto(createdFile.getId(), createdFile.getFile(), createdFile.getAnswersCount(), createdFile.getCreationTime());
        return dto;
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

    @PostMapping("/file/{survey_id}")
    public void addCreatedFile(@PathVariable("survey_id") UUID surveyId,
                               @RequestBody FileInfoDto body) {
        dbService.addCreatedFile(surveyId, body.file(), body.answersCount());
    }

    @PatchMapping("/file/{survey_id}")
    public void updateCreatedFile(@PathVariable("survey_id") UUID surveyId,
                               @RequestBody FileInfoDto body) {
        dbService.updateCreatedFile(surveyId, body.file(), body.answersCount());
    }
}
