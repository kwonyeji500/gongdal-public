package com.example.gongdal.controller.schedule.response;

import com.example.gongdal.dto.comment.CommentGetDto;
import com.example.gongdal.dto.schedule.GetScheduleDetailDto;
import com.example.gongdal.entity.comment.Comment;
import com.example.gongdal.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetScheduleDetailResponse {
    private Long scheduleId;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private Long groupId;
    private String correctorNickname;
    private String writeNickname;
    private boolean editable;
    private byte[] file;

    public static GetScheduleDetailResponse toRes(GetScheduleDetailDto dto) {
        return GetScheduleDetailResponse.builder()
                .scheduleId(dto.getScheduleId())
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .groupId(dto.getGroupId())
                .correctorNickname(dto.getCorrectorNickname())
                .file(dto.getFile())
                .writeNickname(dto.getWriteNickname())
                .editable(dto.isEditable())
                .build();
    }
}
