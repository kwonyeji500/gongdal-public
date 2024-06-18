package com.example.gongdal.dto.group.command;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupExitCommand {
    private User user;
    private Long groupId;

    public static GroupExitCommand toCommand(User user, Long groupId) {
        return GroupExitCommand.builder()
                .user(user)
                .groupId(groupId)
                .build();
    }
}
