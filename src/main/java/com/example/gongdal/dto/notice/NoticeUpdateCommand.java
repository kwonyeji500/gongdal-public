package com.example.gongdal.dto.notice;

import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeUpdateCommand {
    private User user;

    public static NoticeUpdateCommand toCommand(User user) {
        return NoticeUpdateCommand.builder()
                .user(user)
                .build();
    }
}
