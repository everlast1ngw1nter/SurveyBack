package com.example.demo;

import com.example.demo.dto.AccessData;
import com.example.demo.dto.AccessStatus;
import com.example.demo.dto.AcсessDataResponce;
import com.example.demo.models.*;
import com.example.demo.repos.CreatedFilesRepository;
import com.example.demo.repos.ResponseRepository;
import com.example.demo.repos.SurveyRepository;
import com.example.demo.repos.UserRepository;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DbService {

    private final CreatedFilesRepository createdFilesRepository;

    private final SurveyRepository surveyRepository;

    private final ResponseRepository responseRepository;

    private final UserRepository userRepository;

    @Autowired
    public DbService(CreatedFilesRepository createdFilesRepository,
                     SurveyRepository surveyRepository, ResponseRepository responseRepository, UserRepository userRepository) {
        this.createdFilesRepository = createdFilesRepository;
        this.surveyRepository = surveyRepository;
        this.responseRepository = responseRepository;
        this.userRepository = userRepository;
    }

    public Survey getSurvey(UUID surveyId) {
        var survey = surveyRepository.getSurveyById(surveyId);
        return survey;
    }

    public List<Response> getResponses(UUID surveyId) {
        var resps = responseRepository.getResponsesBySurveyId(surveyId);
        return resps;
    }

    public CreatedFile getCreatedFile(UUID surveyId, TypeFile typeFile) {
        var createdFile = createdFilesRepository.getCreatedFileBySurveyIdAndTypeFile(surveyId, typeFile);
        return createdFile;
    }


    public void addCreatedFile(UUID surveyId, byte[] file, Integer answersCount, TypeFile typeFile) {
        var survey = getSurvey(surveyId);
        var newCreatedFile = new CreatedFile(file, answersCount, LocalDateTime.now(), survey, typeFile);
        createdFilesRepository.save(newCreatedFile);
    }

    public void updateCreatedFile(UUID surveyId, byte[] file, Integer answersCount, TypeFile typeFile) {
        var oldFile = getCreatedFile(surveyId, typeFile);
        oldFile.setFile(file);
        oldFile.setAnswersCount(answersCount);
        oldFile.setCreationTime(LocalDateTime.now());
        createdFilesRepository.save(oldFile);
    }

    public String addUser(User user) {
        userRepository.save(user);
        return user.getId().toString();
    }

    public String addUser(String email) {
        var user = new User(email);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            return "User with this email already exists";
        }
        return user.getId().toString();
    }

    public User getUser(String email) {
        return userRepository.getUserByEmail(email);
    }

    public UUID addSurvey(String email, String survey) {
        var user = userRepository.getUserByEmail(email);
        var newSurvey = new Survey(survey, user);
        surveyRepository.save(newSurvey);
        return newSurvey.getId();
    }

    public void addSurveyAccess(UUID surveyId, AccessData body) {
        var survey = surveyRepository.getSurveyById(surveyId);
        survey.setStatus(body.status());
        survey.setLimited(body.isLimited());
        if (body.timeIntervals() != null && body.timeIntervals().size() == 2) {
            survey.setFrom(body.timeIntervals().get(0));
            survey.setTo(body.timeIntervals().get(1));
        }
        surveyRepository.save(survey);
    }

    public boolean isAvailableSurvey(Survey survey){
        if (Objects.equals(survey.getStatus(), AccessStatus.Active.toString()))
            if(!survey.isLimited())
                return true;
            else if (survey.getFrom().isBefore(ZonedDateTime.now()) && survey.getTo().isAfter(ZonedDateTime.now()) ) {
                return true;
            }
        return false;
    }

    public AcсessDataResponce getSurveyAccess(UUID surveyId) {
        var survey = surveyRepository.getSurveyById(surveyId);
        var from = survey.getFrom() == null ? "" : toString();
        var to = survey.getTo() == null ? "" : toString();

        return new AcсessDataResponce(isAvailableSurvey(survey),
                survey.isLimited(), from, to);
    }

    public List<Survey> getSurveys(String email) {
        var surveys = surveyRepository.getSurveyByUserEmail(email);
        return surveys.stream()
                .filter(s -> !s.isDeleted())
                .toList();
    }

    public Long addResponseOnSurvey(String email, UUID surveyId, String body) {
        var user = userRepository.getUserByEmail(email);
        var survey = surveyRepository.getSurveyById(surveyId);
        var response = new Response(user, survey, body);
        responseRepository.save(response);
        return response.getId();
    }

    public void deleteSurvey(UUID surveyId) {
        var toDelete = surveyRepository.getSurveyById(surveyId);
        toDelete.setDeleted(true);
        surveyRepository.save(toDelete);
    }

    public void updateSurvey(UUID surveyId, String body) {
        var survey = surveyRepository.getSurveyById(surveyId);
        survey.setSurvey(body);
        surveyRepository.save(survey);
    }
}
