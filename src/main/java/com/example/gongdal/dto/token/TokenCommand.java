package com.example.gongdal.dto.token;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class TokenCommand {
    private String accessToken;
    private String refreshToken;
    private User user;

    public static TokenCommand toCommand(String accessToken, String refreshToken) {
        return TokenCommand.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static TokenCommand renew(String accessToken, String refreshToken, User user) {
        return TokenCommand.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }
}
