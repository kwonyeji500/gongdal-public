package com.example.gongdal.dto.comment;

import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class CommentGetChildrenCommand {
    private User user;
    private Long scheduleId;
    private Long commentId;
    private Pageable pageable;

    public static CommentGetChildrenCommand toCommand(User user, long scheduleId, long commentId, Pageable pageable) {
        return CommentGetChildrenCommand.builder()
                .user(user)
                .scheduleId(scheduleId)
                .commentId(commentId)
                .pageable(pageable)
                .build();
    }
}
