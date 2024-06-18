package com.example.gongdal.controller.user.response;

import com.example.gongdal.dto.user.UserGroupInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserGroupInfoResponse {
    private Long groupId;
    private String name;
    private String color;
    private String leaderNickname;
    private String createDate;
    private String key;
    private byte[] cover;
    private byte[] leaderProfile;

    public static UserGroupInfoResponse toRes(UserGroupInfoDto dto) {
        return UserGroupInfoResponse.builder()
                .groupId(dto.getGroupId())
                .name(dto.getName())
                .color(dto.getColor())
                .leaderNickname(dto.getLeaderNickname())
                .createDate(dto.getCreateDate())
                .key(dto.getKey())
                .cover(dto.getCover())
                .leaderProfile(dto.getLeaderProfile())
                .build();
    }
}
