package org.example.models;

import jakarta.persistence.*;

@Entity
@Table(name = "responses")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Survey survey;

    @Column(columnDefinition = "TEXT")
    private String response;

    public Response() {
    }

    public Response(User user, Survey survey, String response) {
        this.user = user;
        this.survey = survey;
        this.response = response;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Survey getSurvey() {
        return survey;
    }

    public String getResponse() {
        return response;
    }
}
