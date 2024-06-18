package com.example.gongdal.dto.user.command;

import com.example.gongdal.controller.user.request.LoginUserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class NormalUserLoginCommand {
    private String loginId;
    private String password;
    private String fcmToken;

    public static NormalUserLoginCommand toCommand(LoginUserRequest request) {
        return NormalUserLoginCommand.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .fcmToken(request.getFcmToken())
                .build();
    }
}
