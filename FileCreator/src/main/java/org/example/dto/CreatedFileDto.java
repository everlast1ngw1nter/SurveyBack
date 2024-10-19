package org.example.dto;

import java.time.LocalDateTime;

public record CreatedFileDto(Long id, byte[] file, Integer answersCount, LocalDateTime creationTime) {
}
