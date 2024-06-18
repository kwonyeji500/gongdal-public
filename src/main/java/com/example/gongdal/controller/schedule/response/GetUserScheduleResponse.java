package com.example.gongdal.controller.schedule.response;

import com.example.gongdal.entity.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class GetUserScheduleResponse {
    private Long scheduleId;
    private String name;
    private String startDate;
    private String endDate;
    private String color;
    private Long groupId;

    public static GetUserScheduleResponse toRes(Schedule schedule) {
        return GetUserScheduleResponse.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getName())
                .startDate(schedule.getStartDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .endDate(schedule.getEndDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .color(schedule.getGroup() != null ? schedule.getGroup().getColor() : null)
                .groupId(schedule.getGroup() != null ? schedule.getGroup().getId() : null)
                .build();
    }
}
