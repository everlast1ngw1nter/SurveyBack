package org.example;

import org.example.repos.CreatedFilesRepository;
import org.example.repos.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {

    private final CreatedFilesRepository createdFilesRepository;

    private final SurveyRepository surveyRepository;

    @Autowired
    public DbService(CreatedFilesRepository createdFilesRepository,
                     SurveyRepository surveyRepository) {
        this.createdFilesRepository = createdFilesRepository;
        this.surveyRepository = surveyRepository;
    }

    public String getSurvey(String email, Long surveyId) {
        var survey = surveyRepository.getSurveyByIdAndUserEmail(surveyId, email);
        return survey.getSurvey();
    }
}
