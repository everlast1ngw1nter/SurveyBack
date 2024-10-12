package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ExcelService {

    private final DbService dbService;

    @Autowired
    public ExcelService(DbService dbService) {
        this.dbService = dbService;
    }

    public String generateExcelFile(String email, long surveyId) {
        return dbService.getSurvey(email, surveyId);
    }
}
