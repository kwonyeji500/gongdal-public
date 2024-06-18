package com.example.gongdal.dto.token;

import com.example.gongdal.controller.user.request.TokenRenewRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class TokenRenewCommand {
    private HttpServletRequest request;
    private String refreshToken;

    public static TokenRenewCommand toCommand(HttpServletRequest request, TokenRenewRequest renewRequest) {
        return TokenRenewCommand.builder()
                .request(request)
                .refreshToken(renewRequest.getRefreshToken())
                .build();
    }
}
