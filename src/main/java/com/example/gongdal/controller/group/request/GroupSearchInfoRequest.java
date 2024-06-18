package com.example.gongdal.controller.group.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class GroupSearchInfoRequest {
    @NotBlank(message = "key는 null과 \"\"과 \" \"를 허용하지 않습니다.")
    private String key;
}
