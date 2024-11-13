package com.example.demo.controllers;

import com.example.demo.services.ExcelService;
import com.example.demo.services.PdfService;
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
//@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    private final ExcelService excelService;

    private final PdfService pdfService;

    @Autowired
    public FileController(ExcelService excelService, PdfService pdfService) {
        this.excelService = excelService;
        this.pdfService = pdfService;
    }

    @GetMapping("/survey/{survey_id}/generate_excel")
    public ResponseEntity<byte[]> getExcelFileOnResponses(@PathVariable("survey_id") UUID surveyId) {
        var generated = excelService.getFile(surveyId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=sample.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(generated, headers, HttpStatus.OK);
    }

    @GetMapping("/survey/{survey_id}/generate_pdf")
    public ResponseEntity<byte[]> getPdfFileOnResponses(@PathVariable("survey_id") UUID surveyId) {
        var generated = pdfService.getFile(surveyId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=sample.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(generated, headers, HttpStatus.OK);
    }
}
