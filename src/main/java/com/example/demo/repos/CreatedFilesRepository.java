package com.example.demo.repos;

import com.example.demo.models.CreatedFile;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatedFilesRepository extends JpaRepository<CreatedFile, Long> {

    CreatedFile getCreatedFileBySurveyId(UUID survey_id);
}
