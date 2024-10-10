package com.example.demo.repos;

import com.example.demo.models.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Survey getSurveyByIdAndUserEmail(Long id, String email);

    Survey getSurveyById(Long id);

    List<Survey> getSurveyByUserEmail(String email);
}
