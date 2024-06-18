package com.example.gongdal.dto.group;

import com.example.gongdal.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class GroupSearchInfoDto {
    private Long groupId;
    private String name;
    private String color;
    private String description;
    private String leaderNickname;
    private int participants;
    private String createDate;
    private byte[] cover;

    public static GroupSearchInfoDto toCoverDto(Group group, byte[] cover) {
        return GroupSearchInfoDto.builder()
                .groupId(group.getId())
                .name(group.getName())
                .color(group.getColor())
                .description(group.getDescription())
                .leaderNickname(group.getLeader().getNickname())
                .participants(group.getUsers().size())
                .createDate(group.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .cover(cover)
                .build();
    }

    public static GroupSearchInfoDto toDto(Group group) {
        return GroupSearchInfoDto.builder()
                .groupId(group.getId())
                .name(group.getName())
                .color(group.getColor())
                .description(group.getDescription())
                .leaderNickname(group.getLeader().getNickname())
                .participants(group.getUsers().size())
                .createDate(group.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .build();
    }
}
