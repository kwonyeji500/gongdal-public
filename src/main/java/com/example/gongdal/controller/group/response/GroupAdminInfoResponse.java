package com.example.gongdal.controller.group.response;

import com.example.gongdal.dto.group.GroupAdminInfoDto;
import com.example.gongdal.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupAdminInfoResponse {
    private String name;
    private String color;
    private String description;
    private byte[] cover;

    public static GroupAdminInfoResponse toRes(GroupAdminInfoDto group) {
        return GroupAdminInfoResponse.builder()
                .name(group.getName())
                .color(group.getColor())
                .description(group.getDescription())
                .build();
    }
}
