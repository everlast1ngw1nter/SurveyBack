package com.example.demo.controllers;

import com.example.demo.DbService;
import com.example.demo.models.TypeFile;
import com.example.demo.services.CheckingAvailabilityService;
import com.example.demo.services.ExcelService;
import com.example.demo.services.PdfService;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    private final DbService dbService;

    private final ExcelService excelService;

    private final PdfService pdfService;

    private final CheckingAvailabilityService checkingAvailabilityService;

    @Autowired
    public FileController(DbService dbService,
                          ExcelService excelService,
                          PdfService pdfService,
                          CheckingAvailabilityService checkingAvailabilityService) {
        this.dbService = dbService;
        this.excelService = excelService;
        this.pdfService = pdfService;
        this.checkingAvailabilityService = checkingAvailabilityService;
    }

    @GetMapping("/survey/{survey_id}/report/{report_id}/try_get")
    public ResponseEntity<byte[]> tryGetFileOnResponses(@PathVariable("survey_id") UUID surveyId,
                                                        @PathVariable("report_id") UUID reportId) {
        if (!checkingAvailabilityService.isAvailableSurvey(surveyId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        var report = dbService.getCreatedFile(reportId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Report-Status", report.getReportStatus().name());
        switch (report.getTypeFile()) {
            case TypeFile.PDF -> {
                headers.add("Content-Disposition", "attachment; filename=sample.pdf");
                headers.add("Content-Type", "application/pdf");
            }
            case TypeFile.EXCEL -> {
                headers.add("Content-Disposition", "attachment; filename=sample.xlsx");
                headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }
        }
        return new ResponseEntity<>(report.getFile(), headers, HttpStatus.OK);
    }

    @GetMapping("/survey/{survey_id}/generate_excel")
    public ResponseEntity<UUID> getExcelFileOnResponses(@PathVariable("survey_id") UUID surveyId) {
        if (!checkingAvailabilityService.isAvailableSurvey(surveyId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        var reportId = dbService.addProcessingFile(surveyId, TypeFile.EXCEL);
        CompletableFuture.runAsync(() -> excelService.getFile(surveyId, reportId));
        return new ResponseEntity<>(reportId, HttpStatus.OK);
    }

    @GetMapping("/survey/{survey_id}/generate_pdf")
    public ResponseEntity<UUID> getPdfFileOnResponses(@PathVariable("survey_id") UUID surveyId) {
        if (!checkingAvailabilityService.isAvailableSurvey(surveyId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        var reportId = dbService.addProcessingFile(surveyId, TypeFile.PDF);
        CompletableFuture.runAsync(() -> pdfService.getFile(surveyId, reportId));
        return new ResponseEntity<>(reportId, HttpStatus.OK);
    }
}
