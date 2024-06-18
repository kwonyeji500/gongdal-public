package com.example.gongdal.controller.group.response;

import com.example.gongdal.dto.group.GroupMemberListDto;
import com.example.gongdal.dto.user.UserInfoDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class GroupMemberListResponse {
    private boolean kick;
    private boolean auth;
    private Page<UserInfoDto> users;

    public static GroupMemberListResponse toRes(GroupMemberListDto dto) {
        return GroupMemberListResponse.builder()
                .kick(dto.isKick())
                .auth(dto.isAuth())
                .users(dto.getUsers())
                .build();
    }
}
