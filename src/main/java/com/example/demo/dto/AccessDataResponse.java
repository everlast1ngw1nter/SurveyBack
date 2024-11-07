package com.example.demo.dto;

public record AccessDataResponse(boolean isAvailable, boolean isLimited, String startTime, String endTime) {
}
