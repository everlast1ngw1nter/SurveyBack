package com.example.demo;

import com.example.demo.models.Response;
import com.example.demo.models.Survey;
import com.example.demo.models.User;
import com.example.demo.repos.ResponseRepository;
import com.example.demo.repos.SurveyRepository;
import com.example.demo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class DbService {

    private final ResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    @Autowired
    public DbService(ResponseRepository responseRepository,
                     SurveyRepository surveyRepository,
                     UserRepository userRepository) {
        this.responseRepository = responseRepository;
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
    }

    public String addUser(String email) {
        var user = new User(email);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            return "User with this email already exist";
        }
        return user.getId().toString();
    }

    public Long addNewSurvey(String email, String survey) {
        var user = userRepository.getUserByEmail(email);
        var newSurvey = new Survey(survey, user);
        surveyRepository.save(newSurvey);
        return newSurvey.getId();
    }

    public String getSurvey(String email, Long surveyId) {
        var user = userRepository.getUserByEmail(email);
        var survey = surveyRepository.getSurveyByIdAndUser(surveyId, user);
        return survey.getSurvey();
    }

    public Long addResponseOnSurvey(String email, Long surveyId, String body) {
        var user = userRepository.getUserByEmail(email);
        var survey = surveyRepository.getSurveyById(surveyId);
        var response = new Response(user, survey, body);
        responseRepository.save(response);
        return response.getId();
    }
}
