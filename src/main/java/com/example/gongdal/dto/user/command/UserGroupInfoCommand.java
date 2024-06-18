package com.example.gongdal.dto.user.command;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserGroupInfoCommand {
    private User user;

    public static UserGroupInfoCommand toCommand(User user) {
        return UserGroupInfoCommand.builder()
                .user(user)
                .build();
    }
}
