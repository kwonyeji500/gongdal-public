package com.example.gongdal.dto.group.command;

import com.example.gongdal.controller.group.request.GroupMemberListRequest;
import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class GroupMemberListCommand {
    private User user;
    private Long groupId;
    private String nickname;
    private Pageable pageable;

    public static GroupMemberListCommand toCommand(User user, Long groupId, GroupMemberListRequest request,
                                                   Pageable pageable) {
        return GroupMemberListCommand.builder()
                .user(user)
                .groupId(groupId)
                .nickname(request.getNickname())
                .pageable(pageable)
                .build();
    }
}
