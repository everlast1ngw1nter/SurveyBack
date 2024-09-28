package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbService {

    private final JdbcTemplate jdbcTemplateMap;

    public DbService(@Autowired JdbcTemplate jdbcTemplateMap) {
        this.jdbcTemplateMap = jdbcTemplateMap;
    }

    public String addUser(String email) {
        var id = String.valueOf(email.hashCode());
        try {
            jdbcTemplateMap.update(
                    "INSERT INTO " +
                            "users (id, email) " +
                            "VALUES (?, ?)",
                    id, email);
        } catch (DuplicateKeyException ignored) {

        }
        return id;
    }

    public String getUserIdByEmail(String email) {
        var rowSet = jdbcTemplateMap.queryForRowSet("SELECT * FROM users WHERE users.email = ?", email);
        rowSet.next();
        var id = rowSet.getString("id");
        return id;
    }

    public String addNewSurvey(String email, String survey) {
        var userId = getUserIdByEmail(email);
        var id = String.valueOf(survey.hashCode());
        try {
            jdbcTemplateMap.update(
                    "INSERT INTO " +
                            "surveys (id, user_id, survey) " +
                            "VALUES (?, ?, ?)",
                    id, userId, survey);
        } catch (DuplicateKeyException ignored) {

        }
        return id;
    }

    public String getSurvey(String email, String survey_id) {
        var userId = getUserIdByEmail(email);
        var rowSet = jdbcTemplateMap.queryForRowSet("SELECT * FROM surveys WHERE id = ? and user_id = ?",
                survey_id, userId);
        rowSet.next();
        var survey = rowSet.getString("survey");
        return survey;
    }

    public String addResponseOnSurvey(String email, String survey_id, String body) {
        var userId = getUserIdByEmail(email);
        var id = String.valueOf(body.hashCode());
        try {
            jdbcTemplateMap.update(
                    "INSERT INTO " +
                            "responses (id, survey_id, user_id, response) " +
                            "VALUES (?, ?, ?, ?)",
                    id, survey_id, userId, body);
        } catch (DuplicateKeyException ignored) {

        }
        return id;
    }
}
