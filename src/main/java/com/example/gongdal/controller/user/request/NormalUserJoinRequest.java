package com.example.gongdal.controller.user.request;


import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;


@Getter
@ToString
public class NormalUserJoinRequest {
    @NotBlank(message = "아이디는 null과 \"\"과 \" \"를 허용하지 않습니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 null과 \"\"과 \" \"를 허용하지 않습니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "8자이상의 하나 이상의 문자,숫자,특수문자로 이루어져야 합니다.")
    private String password;
}
