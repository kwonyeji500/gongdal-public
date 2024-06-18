package com.example.gongdal.dto.schedule.command;

import com.example.gongdal.controller.schedule.request.UpdateScheduleRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UpdateScheduleCommand {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private User user;
    private Long scheduleId;
    private MultipartFile file;

    public static UpdateScheduleCommand toCommand(UpdateScheduleRequest request, User user, long scheduleId, MultipartFile file) {
        return UpdateScheduleCommand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .user(user)
                .scheduleId(scheduleId)
                .file(file)
                .build();
    }
}
