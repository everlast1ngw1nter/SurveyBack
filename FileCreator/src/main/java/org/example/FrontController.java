package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FrontController {

    private final ExcelService excelService;

    @Autowired
    public FrontController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/user/{email}/survey/{survey_id}/generate")
    public String getExcelFileOnResponses(@PathVariable("email") String email,
                                          @PathVariable("survey_id") Long surveyId) {
        var generated = excelService.getExcelFile(email, surveyId);
        return generated;
    }
}
