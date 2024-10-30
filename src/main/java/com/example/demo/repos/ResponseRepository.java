package com.example.demo.repos;

import java.util.List;
import java.util.UUID;
import com.example.demo.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    List<Response> getResponsesBySurveyId(UUID survey_id);
}
