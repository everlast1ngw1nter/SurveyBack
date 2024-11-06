package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "created_files")
public class CreatedFile {

    @Id
    private UUID id;

    private Integer answersCount;

    private LocalDateTime creationTime;

    @ManyToOne
    private Survey survey;

    private byte[] file;

    private TypeFile typeFile;

    public CreatedFile() {
    }

    public CreatedFile(byte[] file, Integer answersCount, LocalDateTime creationTime, Survey survey, TypeFile typeFile) {
        id = survey.getId();
        this.file = file;
        this.answersCount = answersCount;
        this.creationTime = creationTime;
        this.survey = survey;
        this.typeFile = typeFile;
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

    public Integer getAnswersCount() {
        return answersCount;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setAnswersCount(Integer answersCount) {
        this.answersCount = answersCount;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public TypeFile getTypeFile() { return typeFile; }

    public void setTypeFile(TypeFile typeFile) { this.typeFile = typeFile; }
}
