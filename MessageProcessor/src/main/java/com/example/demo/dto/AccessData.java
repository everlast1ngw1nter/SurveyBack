package com.example.demo.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record AccessData(AccessStatus status, boolean isLimited, List<ZonedDateTime> timeIntervals) {
}
