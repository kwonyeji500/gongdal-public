package com.example.gongdal.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    INACTIVE("refuse", "탈퇴"),
    ACTIVE("normal", "정상");

    private final String name;
    private final String description;
}
