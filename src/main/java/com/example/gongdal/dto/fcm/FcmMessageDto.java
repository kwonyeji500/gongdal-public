package com.example.gongdal.dto.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmMessageDto {
    private boolean validateOnly;
    private FcmMessageDto.Message message;

    public static FcmMessageDto toDto(FcmSendDto fcmSendDto) {
        return FcmMessageDto.builder()
                .message(FcmMessageDto.Message.toMessage(fcmSendDto))
                .validateOnly(false)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private FcmMessageDto.Notification notification;
        private String token;
        private String topic;

        public static Message toMessage(FcmSendDto fcmSendDto) {
            return Message.builder()
                    .token(fcmSendDto.isTopic() ? null : fcmSendDto.getTarget())
                    .topic(fcmSendDto.isTopic() ? fcmSendDto.getTarget() : null)
                    .notification(FcmMessageDto.Notification.toNoti(fcmSendDto))
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;

        public static Notification toNoti(FcmSendDto fcmSendDto) {
            return Notification.builder()
                    .title(fcmSendDto.getTitle())
                    .body(fcmSendDto.getBody())
                    .build();
        }
    }
}
