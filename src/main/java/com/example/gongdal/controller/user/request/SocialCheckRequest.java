package com.example.gongdal.controller.user.request;

import com.example.gongdal.entity.user.UserType;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class SocialCheckRequest {
    @NotBlank(message = "id_token은 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private String idToken;
    @NotNull
    private UserType type;
    @NotBlank(message = "알림토큰은 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private String fcmToken;
}
