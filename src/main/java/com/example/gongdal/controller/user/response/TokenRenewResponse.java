package com.example.gongdal.controller.user.response;

import com.example.gongdal.dto.token.TokenCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenRenewResponse {
    private String accessToken;
    private String refreshToken;

    public static TokenRenewResponse toRes(TokenCommand command) {
        return TokenRenewResponse.builder()
                .accessToken(command.getAccessToken())
                .refreshToken(command.getRefreshToken())
                .build();
    }
}
