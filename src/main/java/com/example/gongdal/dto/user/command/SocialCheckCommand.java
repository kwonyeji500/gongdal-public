package com.example.gongdal.dto.user.command;

import com.example.gongdal.controller.user.request.SocialCheckRequest;
import com.example.gongdal.entity.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class SocialCheckCommand {
    private String idToken;
    private UserType type;
    private String fcmToken;

    public static SocialCheckCommand toCommand(SocialCheckRequest request) {
        return SocialCheckCommand.builder()
                .idToken(request.getIdToken())
                .type(request.getType())
                .fcmToken(request.getFcmToken()).build();
    }
}
