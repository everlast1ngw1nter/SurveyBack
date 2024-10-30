package org.example.controllers;

import java.util.List;
import java.util.UUID;
import org.example.DbService;
import org.example.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResponseController {

    private final DbService dbService;

    @Autowired
    public ResponseController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping("/responses/{survey_id}")
    public List<ResponseDto> getResponses(@PathVariable("survey_id") UUID surveyId) {
        var responses = dbService.getResponses(surveyId);
        var repsArr = responses.stream()
                .map(x -> new ResponseDto(x.getResponse()))
                .toList();
        return repsArr;
    }
}
