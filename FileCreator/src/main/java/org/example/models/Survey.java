package org.example.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "surveys")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }
}
