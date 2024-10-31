package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.AnswersDto;
import com.example.demo.dto.SurveyDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
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
            PdfPTable table = new PdfPTable(3);
            addTableHeader(table);
            addRows(table);
            addCustomRows(table);
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("column header 1", "column header 2", "column header 3")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
//                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table) {
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }

    private void addCustomRows(PdfPTable table) {

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

    @Override
    public byte[] getFile(UUID surveyId) {
        var reportData = answersDataService.getSurveyAndAnswers(surveyId);
        var questions = reportData.surveyInfo();
        var answers = reportData.answers();
        return generatePdfFile(questions,  answers);
    }
}
