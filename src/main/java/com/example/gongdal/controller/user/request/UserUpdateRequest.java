package com.example.gongdal.controller.user.request;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class UserUpdateRequest {
    private String nickname;
    private LocalDate birth;
}
