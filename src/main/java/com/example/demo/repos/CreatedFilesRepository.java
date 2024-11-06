package com.example.demo.repos;

import com.example.demo.models.CreatedFile;
import java.util.UUID;

import com.example.demo.models.TypeFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatedFilesRepository extends JpaRepository<CreatedFile, Long> {

    CreatedFile getCreatedFileBySurveyIdAndTypeFile(UUID survey_id, TypeFile type_file);
}
