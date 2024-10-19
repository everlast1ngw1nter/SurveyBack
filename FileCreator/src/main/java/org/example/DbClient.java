package org.example;

import java.util.List;
import org.example.dto.CreatedFileDto;
import org.example.dto.FileInfoDto;
import org.example.dto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DbClient {

    private final WebClient dbWebClient;


    public DbClient(WebClient dbWebClient) {
        this.dbWebClient = dbWebClient;
    }

    public String getSurvey(Long id) {
        return dbWebClient
                .get()
                .uri("/survey/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<ResponseDto> getResponses(Long surveyId) {
        return dbWebClient
                .get()
                .uri("/responses/{survey_id}", surveyId)
                .retrieve()
                .bodyToFlux(ResponseDto.class)
                .collectList()
                .block();
    }

    public CreatedFileDto getCreatedFile(Long surveyId) {
        return dbWebClient
                .get()
                .uri("/file/{survey_id}", surveyId)
                .retrieve()
                .bodyToMono(CreatedFileDto.class)
                .block();
    }

    public void updateCreatedFile(CreatedFileDto oldFile, byte[] file, Integer answersCount) {
        dbWebClient
                .patch()
                .uri("/file/{survey_id}", oldFile.id())
                .bodyValue(new FileInfoDto(file, answersCount))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }


    public void addCreatedFile(byte[] file, Integer answersCount, Long surveyId) {
        dbWebClient
                .post()
                .uri("/file/{survey_id}", surveyId)
                .bodyValue(new FileInfoDto(file, answersCount))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}

