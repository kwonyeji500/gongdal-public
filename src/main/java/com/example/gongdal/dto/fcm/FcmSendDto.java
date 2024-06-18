package com.example.gongdal.dto.fcm;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmSendDto {
    private String target;
    private String title;
    private String body;
    private boolean isTopic;

    public static FcmSendDto toSchedule(String key, String message) {
        return FcmSendDto.builder()
                .target(key)
                .title("새로운 일정이 등록되었습니다.")
                .body(message)
                .isTopic(true).build();
    }

    public static FcmSendDto toComment(String fcmToken, String message) {
        return FcmSendDto.builder()
                .target(fcmToken)
                .title("새로운 댓글이 등록되었습니다.")
                .body(message)
                .isTopic(false).build();
    }
}
