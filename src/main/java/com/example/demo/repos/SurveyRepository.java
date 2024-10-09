package com.example.demo.repos;

import com.example.demo.models.Survey;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Survey getSurveyByIdAndUser(Long id, User user);

    Survey getSurveyById(Long id);

    List<Survey> getSurveyByUser(User user);
}
