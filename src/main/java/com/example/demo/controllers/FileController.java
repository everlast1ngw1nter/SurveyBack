package com.example.demo.controllers;

import com.example.demo.services.ExcelService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    private final ExcelService excelService;

    @Autowired
    public FileController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/user/{email}/survey/{survey_id}/generate")
    public ResponseEntity<byte[]> getExcelFileOnResponses(@PathVariable("email") String email,
                                            @PathVariable("survey_id") UUID surveyId) {
        var generated = excelService.getFile(surveyId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=sample.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(generated, headers, HttpStatus.OK);
    }
}
