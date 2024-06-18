package com.example.gongdal.controller.user.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class TokenRenewRequest {
    @NotBlank(message = "refreshToken은 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private String refreshToken;
}
