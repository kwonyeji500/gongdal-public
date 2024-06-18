package com.example.gongdal.dto.user.command;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoCommand {
    private User user;

    public static UserInfoCommand toCommand(User user) {
        return UserInfoCommand.builder()
                .user(user)
                .build();
    }
}
