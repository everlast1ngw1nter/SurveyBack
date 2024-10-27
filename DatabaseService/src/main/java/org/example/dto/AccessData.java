package org.example.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record AccessData(String status, boolean isLimited, List<ZonedDateTime> timeIntervals) {
}
