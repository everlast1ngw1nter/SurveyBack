package com.example.demo.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record AcсessDataResponce(boolean isAvailable, boolean isLimited, String startTime, String endTime) {
}
