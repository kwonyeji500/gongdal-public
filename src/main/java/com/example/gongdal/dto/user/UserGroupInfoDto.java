package com.example.gongdal.dto.user;

import com.example.gongdal.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@Builder
public class UserGroupInfoDto {
    private Long groupId;
    private String name;
    private String color;
    private String leaderNickname;
    private String createDate;
    private String key;
    private byte[] cover;
    private byte[] leaderProfile;

    public static UserGroupInfoDto toDto(Group group, byte[] cover, byte[] leaderProfile) {
        return UserGroupInfoDto.builder()
                .groupId(group.getId())
                .name(group.getName())
                .color(group.getColor())
                .leaderNickname(group.getLeader().getNickname())
                .createDate(group.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .key(group.getKey())
                .cover(cover)
                .leaderProfile(leaderProfile)
                .build();
    }
}
