package com.example.gongdal.dto.user.command;

import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDeleteProfileCommand {
    private User user;

    public static UserDeleteProfileCommand toCommand(User user) {
        return UserDeleteProfileCommand.builder()
                .user(user)
                .build();
    }
}
