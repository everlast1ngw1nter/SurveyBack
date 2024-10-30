package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.DbClient;
import org.example.dto.AnswersDto;
import org.example.dto.QuestionAnswerDto;
import org.example.dto.ResponseDto;
import org.example.dto.SurveyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

    private final DbClient dbClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public ExcelService(DbClient dbClient, ObjectMapper objectMapper) {
        this.dbClient = dbClient;
        this.objectMapper = objectMapper;
    }

    private SurveyDto getQuestionsFromSurvey(UUID surveyId) {
        var survey = dbClient.getSurvey(surveyId);
        SurveyDto dto;
        try {
            dto = objectMapper.readValue(survey, SurveyDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    private List<AnswersDto> getAnswersFromResponses(UUID surveyId) {
        var resps = dbClient.getResponses(surveyId);
        return resps.stream()
                .map(ResponseDto::response)
                .map(this::getAnswersFromResponse)
                .toList();

    }

    private AnswersDto getAnswersFromResponse(String response) {
        AnswersDto dto;
        try {
            Map<String, QuestionAnswerDto> data = objectMapper.readValue(response, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, QuestionAnswerDto.class));
            var answers = data.values()
                    .stream()
                    .map(qaDto -> qaDto.answer)
                    .toList();
            dto = new AnswersDto(answers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dto;
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
        var sheet = book.createSheet(questions.name);
        var row = sheet.createRow(0);
        addAllQuestions(row, questions);
        addAllAnswersInRows(sheet, answers, 1);
        return book;
    }

    public byte[] getExcelFile(UUID surveyId) {
        var questions = getQuestionsFromSurvey(surveyId);
        var answers = getAnswersFromResponses(surveyId);
        var createdFile = dbClient.getCreatedFile(surveyId);
        if (createdFile != null && createdFile.answersCount() == answers.size()) {
            return createdFile.file();
        }
        var excelBytes = getBytesOfExcelFile(questions, answers);
        if (createdFile != null) {
            dbClient.updateCreatedFile(createdFile, excelBytes, answers.size());
        } else {
            dbClient.addCreatedFile(excelBytes, answers.size(), surveyId);
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
