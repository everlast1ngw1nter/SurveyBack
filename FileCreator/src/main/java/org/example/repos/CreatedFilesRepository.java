package org.example.repos;

import org.example.models.CreatedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatedFilesRepository extends JpaRepository<CreatedFile, Long> {

    CreatedFile getCreatedFileBySurveyId(Long surveyId);
}
