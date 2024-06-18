package com.example.gongdal.dto.notice;

import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class NoticeGetCommand {
    private User user;
    private Pageable pageable;

    public static NoticeGetCommand toCommand(User user, Pageable pageable) {
        return NoticeGetCommand.builder()
                .user(user)
                .pageable(pageable).build();
    }
}
