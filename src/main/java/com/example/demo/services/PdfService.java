package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.AnswersDto;
import com.example.demo.dto.QuestionsDto;
import com.example.demo.dto.SurveyDto;
import com.example.demo.models.TypeFile;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PdfService implements ReportFileService {
    private final AnswersDataService answersDataService;

    private final DbService dbClient;

    @Autowired
    public PdfService(AnswersDataService answersDataService, DbService dbClient) {
        this.answersDataService = answersDataService;
        this.dbClient = dbClient;
    }

    private byte[] generatePdfFile(SurveyDto questions, List<AnswersDto> answers) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var document = new Document();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            PdfPTable table = new PdfPTable(questions.survey.size());
            BaseFont baseFont = BaseFont.createFont("fonts\\ofont.ru_Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont);
            addTableHeader(table, questions.survey, font);
            addRows(table, answers, font);
            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void addTableHeader(PdfPTable table, List<QuestionsDto> questions, Font font) {
        questions
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle.question, font));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<AnswersDto> answers, Font font) {
        for(var answer : answers) {
            for (var answerOnQuestion : answer.answers) {
                PdfPCell cell = new PdfPCell(new Phrase(answerOnQuestion, font));
                table.addCell(cell);
            }
        }
    }

    @Override
    public byte[] getFile(UUID surveyId) {
        var reportData = answersDataService.getSurveyAndAnswers(surveyId);
        var questions = reportData.surveyInfo();
        var answers = reportData.answers();
        var createdFile = dbClient.getCreatedFile(surveyId, TypeFile.PDF);
        if (createdFile != null && createdFile.getAnswersCount() == answers.size()) {
            return createdFile.getFile();
        }
        var pdfBytes = generatePdfFile(questions,  answers);
        if (createdFile != null) {
            dbClient.updateCreatedFile(createdFile.getId(), pdfBytes, answers.size(), TypeFile.PDF);
        } else {
            dbClient.addCreatedFile(surveyId, pdfBytes, answers.size(), TypeFile.PDF);
        }
        return pdfBytes;
    }
}
