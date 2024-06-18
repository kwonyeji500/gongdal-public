package com.example.gongdal.dto.group.command;

import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class GroupGetFeedCommand {
    private User user;
    private Long groupId;
    private Pageable pageable;

    public static GroupGetFeedCommand toCommand(User user, Long groupId, Pageable pageable) {
        return GroupGetFeedCommand.builder()
                .user(user)
                .groupId(groupId)
                .pageable(pageable)
                .build();
    }
}
