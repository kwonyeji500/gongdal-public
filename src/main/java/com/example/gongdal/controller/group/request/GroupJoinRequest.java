package com.example.gongdal.controller.group.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class GroupJoinRequest {
    @NotBlank(message = "비밀번호는 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private String password;

    @NotBlank(message = "그룹 id는 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private Long groupId;
}
