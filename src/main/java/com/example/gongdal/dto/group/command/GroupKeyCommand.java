package com.example.gongdal.dto.group.command;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupKeyCommand {
    private User user;
    private Long groupId;

    public static GroupKeyCommand toCommand(User user, Long groupId) {
        return GroupKeyCommand.builder()
                .user(user)
                .groupId(groupId)
                .build();
    }
}
