package com.example.gongdal.dto.group.command;

import com.example.gongdal.controller.group.request.GroupKickRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupKickCommand {
    private User user;
    private Long groupId;
    private Long kickUserId;

    public static GroupKickCommand toCommand(User user, GroupKickRequest request) {
        return GroupKickCommand.builder()
                .user(user)
                .groupId(request.getGroupId())
                .kickUserId(request.getKickUserId())
                .build();
    }
}
