package com.example.gongdal.dto.schedule.command;

import com.example.gongdal.controller.schedule.request.GetScheduleUserRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class GetScheduleUserCommand {
    private User user;
    private LocalDate start;
    private LocalDate end;

    public static GetScheduleUserCommand toCommand(User user, GetScheduleUserRequest request) {
        return GetScheduleUserCommand.builder()
                .user(user)
                .start(request.getStart())
                .end(request.getEnd())
                .build();
    }
}
