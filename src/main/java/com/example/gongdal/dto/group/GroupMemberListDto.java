package com.example.gongdal.dto.group;

import com.example.gongdal.dto.user.UserInfoDto;
import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class GroupMemberListDto {
    private boolean kick;
    private boolean auth;
    private Page<UserInfoDto> users;

    public static GroupMemberListDto toDto(boolean kick, boolean auth, Page<UserInfoDto> users) {
        return GroupMemberListDto.builder()
                .kick(kick)
                .auth(auth)
                .users(users)
                .build();
    }
}
