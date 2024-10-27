package org.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.example.dto.AccessData;
import org.example.models.CreatedFile;
import org.example.models.Response;
import org.example.models.Survey;
import org.example.models.User;
import org.example.repos.CreatedFilesRepository;
import org.example.repos.ResponseRepository;
import org.example.repos.SurveyRepository;
import org.example.repos.UserRepository;
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

    public CreatedFile getCreatedFile(UUID surveyId) {
        var createdFile = createdFilesRepository.getCreatedFileBySurveyId(surveyId);
        return createdFile;
    }


    public void addCreatedFile(UUID surveyId, byte[] file, Integer answersCount) {
        var survey = getSurvey(surveyId);
        var newCreatedFile = new CreatedFile(file, answersCount, LocalDateTime.now(), survey);
        createdFilesRepository.save(newCreatedFile);
    }

    public void updateCreatedFile(UUID surveyId, byte[] file, Integer answersCount) {
        var oldFile = getCreatedFile(surveyId);
        oldFile.setFile(file);
        oldFile.setAnswersCount(answersCount);
        oldFile.setCreationTime(LocalDateTime.now());
        createdFilesRepository.save(oldFile);
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

    public List<Survey> getSurveys(String email) {
        var surveys = surveyRepository.getSurveyByUserEmail(email);
        return surveys;
    }

    public Long addResponseOnSurvey(String email, UUID surveyId, String body) {
        var user = userRepository.getUserByEmail(email);
        var survey = surveyRepository.getSurveyById(surveyId);
        var response = new Response(user, survey, body);
        responseRepository.save(response);
        return response.getId();
    }

    public void deleteSurvey(Long surveyId) {
        surveyRepository.deleteById(surveyId);
    }

    public void updateSurvey(UUID surveyId, String body) {
        var survey = surveyRepository.getSurveyById(surveyId);
        survey.setSurvey(body);
        surveyRepository.save(survey);
    }
}
