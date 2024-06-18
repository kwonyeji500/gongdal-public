package com.example.gongdal.dto.comment;

import com.example.gongdal.controller.comment.request.CommentCreateRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentCreateCommand {
    private User user;
    private Long scheduleId;
    private String content;
    private Long parentId;

    public static CommentCreateCommand toCommand(User user, Long scheduleId, CommentCreateRequest request) {
        return CommentCreateCommand.builder()
                .user(user)
                .scheduleId(scheduleId)
                .content(request.getContent())
                .parentId(request.getParentId())
                .build();
    }
}
