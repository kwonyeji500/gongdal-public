package com.example.gongdal.dto.comment;

import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class CommentGetCommand {
    private User user;
    private Long scheduleId;
    private Pageable pageable;

    public static CommentGetCommand toCommand(User user, Long scheduleId, Pageable pageable) {
        return CommentGetCommand.builder()
                .user(user)
                .scheduleId(scheduleId)
                .pageable(pageable)
                .build();
    }
}
