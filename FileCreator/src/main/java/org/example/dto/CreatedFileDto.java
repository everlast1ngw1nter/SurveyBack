package org.example.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreatedFileDto(UUID id, byte[] file, Integer answersCount, LocalDateTime creationTime) {
}
