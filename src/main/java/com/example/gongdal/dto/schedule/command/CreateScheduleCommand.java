package com.example.gongdal.dto.schedule.command;

import com.example.gongdal.controller.schedule.request.CreateScheduleRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CreateScheduleCommand {
    private User user;
    private String name;
    private String description;
    private Long groupId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private MultipartFile file;

    public static CreateScheduleCommand toCommand(CreateScheduleRequest request, User user, MultipartFile file) {
        return CreateScheduleCommand.builder()
                .user(user)
                .name(request.getName())
                .groupId(request.getGroupId())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .file(file)
                .build();
    }
}
