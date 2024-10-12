package org.example.repos;

import java.util.List;
import org.example.models.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Survey getSurveyByIdAndUserEmail(Long id, String email);

    Survey getSurveyById(Long id);

    List<Survey> getSurveyByUserEmail(String email);
}
