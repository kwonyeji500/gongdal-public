package com.example.gongdal.controller.user.response;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginUserResponse {
    private Long userId;
    private String nickname;
    private String accessToken;
    private String refreshToken;

    public static LoginUserResponse toRes(User user) {
        return LoginUserResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .accessToken(user.getAccessToken())
                .refreshToken(user.getRefreshToken())
                .build();
    }
}
