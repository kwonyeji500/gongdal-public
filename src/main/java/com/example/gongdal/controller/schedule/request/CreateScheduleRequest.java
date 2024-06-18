package com.example.gongdal.controller.schedule.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@ToString
public class CreateScheduleRequest {
    @NotBlank
    private String name;
    private String description;
    private Long groupId;
    @NotBlank
    private LocalDateTime startDate;
    @NotBlank
    private LocalDateTime endDate;
}
