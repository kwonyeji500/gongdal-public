package com.example.gongdal.dto.schedule.command;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeleteScheduleFileCommand {
    private User user;
    private Long scheduleId;

    public static DeleteScheduleCommand toCommand(User user, Long scheduleId) {
        return DeleteScheduleCommand.builder()
                .user(user)
                .scheduleId(scheduleId)
                .build();
    }
}
