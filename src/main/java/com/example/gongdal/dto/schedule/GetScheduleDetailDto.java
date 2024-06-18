package com.example.gongdal.dto.schedule;

import com.example.gongdal.entity.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class GetScheduleDetailDto {
    private Long scheduleId;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private Long groupId;
    private String correctorNickname;
    private String writeNickname;
    private String color;
    private boolean editable;
    private byte[] file;

    public static GetScheduleDetailDto toUserDto(Schedule schedule, byte[] file, boolean editable) {
        return GetScheduleDetailDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getName())
                .description(schedule.getDescription())
                .startDate(schedule.getStartDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .endDate(schedule.getEndDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .file(file)
                .writeNickname(schedule.getUser().getNickname())
                .editable(editable)
                .build();
    }

    public static GetScheduleDetailDto toGroupDto(Schedule schedule, byte[] file, boolean editable) {
        return GetScheduleDetailDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getName())
                .description(schedule.getDescription())
                .startDate(schedule.getStartDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .endDate(schedule.getEndDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .groupId(schedule.getGroup().getId())
                .correctorNickname(schedule.getCorrector() == null ? null : schedule.getCorrector().getNickname())
                .file(file)
                .writeNickname(schedule.getGroup() == null ? schedule.getUser().getNickname() : schedule.getWriter().getNickname())
                .editable(editable)
                .color(schedule.getGroup().getColor())
                .build();
    }
}
