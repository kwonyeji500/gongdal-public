package com.example.gongdal.dto.group;

import com.example.gongdal.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupAdminInfoDto {
    private String name;
    private String color;
    private String description;
    private byte[] cover;

    public static GroupAdminInfoDto toCoverDto(Group group, byte[] cover) {
        return GroupAdminInfoDto.builder()
                .name(group.getName())
                .color(group.getColor())
                .description(group.getDescription())
                .cover(cover)
                .build();
    }

    public static GroupAdminInfoDto toDto(Group group) {
        return GroupAdminInfoDto.builder()
                .name(group.getName())
                .color(group.getColor())
                .description(group.getDescription())
                .build();
    }
}
