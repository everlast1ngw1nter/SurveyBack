package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "created_files")
public class CreatedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime creationTime;

    @ManyToOne
    private Survey survey;

    private byte[] file;

    private TypeFile typeFile;

    private ReportStatus reportStatus;

    public CreatedFile() {
    }

    public CreatedFile(byte[] file, LocalDateTime creationTime, Survey survey, TypeFile typeFile, ReportStatus reportStatus) {
        this.file = file;
        this.creationTime = creationTime;
        this.survey = survey;
        this.typeFile = typeFile;
        this.reportStatus = reportStatus;
    }

    public UUID getId() {
        return id;
    }

    public byte[] getFile() {
        return file;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public TypeFile getTypeFile() { return typeFile; }

    public void setTypeFile(TypeFile typeFile) { this.typeFile = typeFile; }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }
}
