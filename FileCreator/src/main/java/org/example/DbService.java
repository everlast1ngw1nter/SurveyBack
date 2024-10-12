package org.example;

import java.util.List;
import org.example.models.Response;
import org.example.repos.CreatedFilesRepository;
import org.example.repos.ResponseRepository;
import org.example.repos.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {

    private final CreatedFilesRepository createdFilesRepository;

    private final SurveyRepository surveyRepository;

    private final ResponseRepository responseRepository;

    @Autowired
    public DbService(CreatedFilesRepository createdFilesRepository,
                     SurveyRepository surveyRepository, ResponseRepository responseRepository) {
        this.createdFilesRepository = createdFilesRepository;
        this.surveyRepository = surveyRepository;
        this.responseRepository = responseRepository;
    }

    public String getSurvey(String email, Long surveyId) {
        var survey = surveyRepository.getSurveyByIdAndUserEmail(surveyId, email);
        return survey.getSurvey();
    }

    public List<Response> getResponses(String email, Long surveyId) {
        var resps = responseRepository.getResponsesBySurveyIdAndUserEmail(surveyId, email);
        return resps;
    }
}
