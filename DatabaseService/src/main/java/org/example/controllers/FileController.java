package org.example.controllers;

import java.util.UUID;
import org.example.DbService;
import org.example.dto.CreatedFileDto;
import org.example.dto.FileInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FileController {

    private final DbService dbService;

    @Autowired
    public FileController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping("/file/{survey_id}")
    public CreatedFileDto getCreatedFile(@PathVariable("survey_id") UUID surveyId) {
        var createdFile = dbService.getCreatedFile(surveyId);
        var dto = new CreatedFileDto(createdFile.getId(), createdFile.getFile(), createdFile.getAnswersCount(), createdFile.getCreationTime());
        return dto;
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
