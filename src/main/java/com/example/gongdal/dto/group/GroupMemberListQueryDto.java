package com.example.gongdal.dto.group;

import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class GroupMemberListQueryDto {
    private Group group;
    private Page<User> users;

    public static GroupMemberListQueryDto toDto(Group group, Page<User> users) {
        return GroupMemberListQueryDto.builder()
                .group(group)
                .users(users)
                .build();
    }
}
