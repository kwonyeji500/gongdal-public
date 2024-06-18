package com.example.gongdal.dto.user.command;

import com.example.gongdal.controller.user.request.UserNoticeUpdateRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class UserNoticeUpdateCommand {
    private User user;
    private boolean notice;

    public static UserNoticeUpdateCommand toCommand(User user, UserNoticeUpdateRequest request) {
        return UserNoticeUpdateCommand.builder()
                .user(user)
                .notice(request.isNotice())
                .build();
    }
}
