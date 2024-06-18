package com.example.gongdal.dto.comment;

import com.example.gongdal.controller.comment.request.CommentUpdateRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentUpdateCommand {
    private User user;
    private String content;
    private Long commentId;

    public static CommentUpdateCommand toCommand(User user, CommentUpdateRequest request, long commentId) {
        return CommentUpdateCommand.builder()
                .user(user)
                .content(request.getContent())
                .commentId(commentId)
                .build();
    }
}
