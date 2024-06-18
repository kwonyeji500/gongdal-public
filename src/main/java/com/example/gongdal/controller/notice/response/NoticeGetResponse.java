package com.example.gongdal.controller.notice.response;

import com.example.gongdal.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class NoticeGetResponse {
    private Long id;
    private String message;
    private boolean active;
    private Long scheduleId;
    private String createDate;
    private Long userId;
    private String type;

    public static NoticeGetResponse toRes(Notice notice) {
        return NoticeGetResponse.builder()
                .id(notice.getId())
                .message(notice.getMessage())
                .active(notice.isActive())
                .scheduleId(notice.getScheduleId())
                .createDate(notice.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .userId(notice.getWriterId())
                .type(notice.getType().getDescription())
                .build();
    }
}
