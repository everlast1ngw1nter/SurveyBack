package org.example.repos;

import java.util.List;
import org.example.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    List<Response> getResponsesBySurveyIdAndUserEmail(Long surveyId, String email);
}
