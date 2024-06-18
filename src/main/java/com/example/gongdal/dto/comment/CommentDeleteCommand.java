package com.example.gongdal.dto.comment;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentDeleteCommand {
    private User user;
    private Long commentId;

    public static CommentDeleteCommand toCommand(User user, long commentId) {
        return CommentDeleteCommand.builder()
                .user(user)
                .commentId(commentId)
                .build();
    }
}
