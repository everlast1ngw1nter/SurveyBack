package org.example.models;

import jakarta.persistence.*;
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

    public CreatedFile() {
    }

    public CreatedFile(byte[] file, Integer answersCount, LocalDateTime creationTime, Survey survey) {
        id = survey.getId();
        this.file = file;
        this.answersCount = answersCount;
        this.creationTime = creationTime;
        this.survey = survey;
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
}
