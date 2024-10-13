package org.example.services;

import java.time.LocalDateTime;
import java.util.List;
import org.example.models.CreatedFile;
import org.example.models.Response;
import org.example.models.Survey;
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

    public Survey getSurvey(Long surveyId) {
        var survey = surveyRepository.getSurveyById(surveyId);
        return survey;
    }

    public List<Response> getResponses(String email, Long surveyId) {
        var resps = responseRepository.getResponsesBySurveyIdAndUserEmail(surveyId, email);
        return resps;
    }

    public CreatedFile getCreatedFile(Long surveyId) {
        var createdFile = createdFilesRepository.getCreatedFileBySurveyId(surveyId);
        return createdFile;
    }


    public void addCreatedFile(byte[] file, Integer answersCount, Long surveyId) {
        var survey = getSurvey(surveyId);
        var newCreatedFile = new CreatedFile(file, answersCount, LocalDateTime.now(), survey);
        createdFilesRepository.save(newCreatedFile);
    }

    public void updateCreatedFile(CreatedFile oldFile, byte[] file, Integer answersCount) {
        oldFile.setFile(file);
        oldFile.setAnswersCount(answersCount);
        oldFile.setCreationTime(LocalDateTime.now());
        createdFilesRepository.save(oldFile);
    }
}
