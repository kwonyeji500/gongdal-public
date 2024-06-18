package com.example.gongdal.controller.group.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class GroupCreateRequest {
    @NotBlank(message = "그룹 이름은 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private String name;

    @NotBlank(message = "그룹 생상은 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private String color;

    private String description;

    @NotBlank(message = "비밀번호는 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private String password;
}
