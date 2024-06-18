package com.example.gongdal.controller.group.response;

import com.example.gongdal.entity.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class GroupGetFeedResponse {
    private Long scheduleId;
    private String nickname;
    private String startDate;
    private String endDate;
    private String scheduleName;
    private String createDate;

    public static GroupGetFeedResponse toRes(Schedule schedule) {
        return GroupGetFeedResponse.builder()
                .scheduleId(schedule.getId())
                .nickname(schedule.getWriter().getNickname())
                .startDate(schedule.getStartDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .endDate(schedule.getEndDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .scheduleName(schedule.getName())
                .createDate(schedule.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .build();
    }
}
