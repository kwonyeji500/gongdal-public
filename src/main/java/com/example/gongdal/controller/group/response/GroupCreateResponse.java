package com.example.gongdal.controller.group.response;

import com.example.gongdal.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupCreateResponse {
    private Long groupId;
    private String key;

    public static GroupCreateResponse toRes(Group group) {
        return GroupCreateResponse.builder()
                .groupId(group.getId())
                .key(group.getKey())
                .build();
    }
}
