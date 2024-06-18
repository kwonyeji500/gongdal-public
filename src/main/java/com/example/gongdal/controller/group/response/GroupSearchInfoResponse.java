package com.example.gongdal.controller.group.response;

import com.example.gongdal.dto.group.GroupSearchInfoDto;
import com.example.gongdal.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GroupSearchInfoResponse {
    private Long groupId;
    private String name;
    private String color;
    private String description;
    private String leaderNickname;
    private int participants;
    private String createDate;
    private byte[] cover;

    public static GroupSearchInfoResponse toRes(GroupSearchInfoDto group) {
        return GroupSearchInfoResponse.builder()
                .groupId(group.getGroupId())
                .name(group.getName())
                .color(group.getColor())
                .description(group.getDescription())
                .leaderNickname(group.getLeaderNickname())
                .participants(group.getParticipants())
                .createDate(group.getCreateDate())
                .cover(group.getCover())
                .build();
    }
}
