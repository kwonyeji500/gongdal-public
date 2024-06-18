package com.example.gongdal.dto.schedule.command;

import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetScheduleDetailCommand {
    private Long scheduleId;
    private User user;

    public static GetScheduleDetailCommand toCommand(Long scheduleId, User user) {
        return GetScheduleDetailCommand.builder()
                .scheduleId(scheduleId)
                .user(user)
                .build();
    }
}
