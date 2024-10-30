package com.example.demo.repos;

import java.util.List;
import java.util.UUID;
import com.example.demo.models.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Survey getSurveyById(UUID id);

    List<Survey> getSurveyByUserEmail(String email);
}
