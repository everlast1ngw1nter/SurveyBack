package org.example.models;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "surveys")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @Column(columnDefinition = "TEXT")
    private String survey;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "survey_id")
    private Set<Response> responses;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "survey_id")
    private Set<CreatedFile> createdFile;

    private String status;

    private boolean isLimited;

    @Column(name = "_from")
    private ZonedDateTime from;

    @Column(name = "_to")
    private ZonedDateTime to;

    public Survey() {
    }

    public Survey(String survey, User user) {
        this.survey = survey;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getSurvey() {
        return survey;
    }

    public UUID getId() {
        return id;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    public ZonedDateTime getFrom() {
        return from;
    }

    public void setFrom(ZonedDateTime from) {
        this.from = from;
    }

    public ZonedDateTime getTo() {
        return to;
    }

    public void setTo(ZonedDateTime to) {
        this.to = to;
    }
}
