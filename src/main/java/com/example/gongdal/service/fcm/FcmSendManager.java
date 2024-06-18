package com.example.gongdal.service.fcm;

import com.example.gongdal.dto.fcm.FcmMessageDto;
import com.example.gongdal.dto.fcm.FcmSendDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmSendManager {

    public void sendMessageTo(FcmSendDto fcmSendDto) {
        String message = makeMessage(fcmSendDto);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        String API_URL = "https://fcm.googleapis.com/v1/projects/gongdal2/messages:send";
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            log.info("[FcmSendMessage] sendMessageTo - target : {}, Status : {}", fcmSendDto.getTarget(), response.getStatusCode());
        } catch (HttpClientErrorException e) {
            log.error("[FcmSendMessage] Error sending FCM message - target: {}, Error: {}", fcmSendDto.getTarget(), e.getRawStatusCode());
            throw new RuntimeException("Failed to send FCM message", e);
        }
    }

    private String getAccessToken() {
        try {
            String firebaseConfigPath = "fcm/GongDal2 Firebase Service Account.json";

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.error("[FcmSendMessage] getAccessToken IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String makeMessage(FcmSendDto fcmSendDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto.toDto(fcmSendDto);
        try {
            return objectMapper.writeValueAsString(fcmMessageDto);
        } catch (JsonProcessingException e) {
            log.error("[FcmSendMessage] makeMessage JsonProcessingException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
