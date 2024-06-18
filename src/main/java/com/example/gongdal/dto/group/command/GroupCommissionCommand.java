package com.example.gongdal.dto.group.command;

import com.example.gongdal.controller.group.request.GroupCommissionRequest;
import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupCommissionCommand {
    private User user;
    private Long groupId;
    private Long targetId;

    public static GroupCommissionCommand toCommand(User user, GroupCommissionRequest request) {
        return GroupCommissionCommand.builder()
                .user(user)
                .groupId(request.getGroupId())
                .targetId(request.getTargetId())
                .build();
    }
}
