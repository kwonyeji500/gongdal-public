package com.example.gongdal.dto.group.command;

import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupDeleteCoverCommand {
    private User user;
    private Long groupId;

    public static GroupDeleteCommand toCommand(User user, Long groupId) {
        return GroupDeleteCommand.builder()
                .user(user)
                .groupId(groupId).build();
    }
}
