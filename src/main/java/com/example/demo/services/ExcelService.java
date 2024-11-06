package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.AnswersDto;
import com.example.demo.dto.SurveyDto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.example.demo.models.TypeFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelService implements ReportFileService {

    private final AnswersDataService answersDataService;

    private final DbService dbClient;

    @Autowired
    public ExcelService(AnswersDataService answersDataService, DbService dbClient) {
        this.answersDataService = answersDataService;
        this.dbClient = dbClient;
    }

    private void addAllQuestions(Row row, SurveyDto questions) {
        for (var i = 0; i < questions.survey.size(); i++) {
            var currentCell = row.createCell(i);
            currentCell.setCellValue(questions.survey.get(i).question);
        }
    }

    private void addAnswersInRow(Row row, AnswersDto answers) {
        for (var i = 0; i < answers.answers.size(); i++) {
            var currentCell = row.createCell(i);
            currentCell.setCellValue(answers.answers.get(i));
        }
    }

    private void addAllAnswersInRows(Sheet sheet, List<AnswersDto> answers, int startRow) {
        for (var i = startRow; i < startRow + answers.size(); i++) {
            addAnswersInRow(sheet.createRow(i), answers.get(i - startRow));
        }
    }

    private Workbook generateExcelFile(SurveyDto questions,  List<AnswersDto> answers) {
        Workbook book = new XSSFWorkbook();
        var sheet = book.createSheet("Answers");
        var row = sheet.createRow(0);
        addAllQuestions(row, questions);
        addAllAnswersInRows(sheet, answers, 1);
        return book;
    }

    public byte[] getFile(UUID surveyId) {
        var reportData = answersDataService.getSurveyAndAnswers(surveyId);
        var questions = reportData.surveyInfo();
        var answers = reportData.answers();
        var createdFile = dbClient.getCreatedFile(surveyId, TypeFile.EXCEL);
        if (createdFile != null && createdFile.getAnswersCount() == answers.size()) {
            return createdFile.getFile();
        }
        var excelBytes = getBytesOfExcelFile(questions, answers);
        if (createdFile != null) {
            dbClient.updateCreatedFile(createdFile.getId(), excelBytes, answers.size(), TypeFile.EXCEL);
        } else {
            dbClient.addCreatedFile(surveyId, excelBytes, answers.size(), TypeFile.EXCEL);
        }
        return excelBytes;
    }

    private byte[] getBytesOfExcelFile(SurveyDto questions, List<AnswersDto> answers) {
        var outputStream = new ByteArrayOutputStream();
        try(var workbook = generateExcelFile(questions,  answers)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }
}
