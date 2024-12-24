package com.example.demo.services;


import java.util.UUID;

@FunctionalInterface
public interface ReportFileService {

    byte[] getFile(UUID surveyId, UUID reportId);


}
