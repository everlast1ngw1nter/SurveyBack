package org.example.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "created_files")
public class CreatedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String file;

    private LocalDateTime creationTime;

    public CreatedFile() {
    }

    public CreatedFile(String file, LocalDateTime creationTime) {
        this.file = file;
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public String getFile() {
        return file;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}
