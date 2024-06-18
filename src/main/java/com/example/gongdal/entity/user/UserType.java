package com.example.gongdal.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    A("apple", "애플"),
    G("google", "구글"),
    K("kakao", "카카오"),
    N("normal", "일반");

    private final String name;
    private final String description;
}
