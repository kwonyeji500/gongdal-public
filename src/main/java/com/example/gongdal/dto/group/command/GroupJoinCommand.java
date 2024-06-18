package com.example.gongdal.dto.group.command;

import com.example.gongdal.controller.group.request.GroupJoinRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupJoinCommand {
    private User user;
    private String password;
    private Long groupId;

    public static GroupJoinCommand toCommand(User user, GroupJoinRequest request) {
        return GroupJoinCommand.builder()
                .user(user)
                .password(request.getPassword())
                .groupId(request.getGroupId())
                .build();
    }
}
