package com.example.gongdal.controller.group.response;

import com.example.gongdal.entity.group.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupKeyResponse {
    private String key;

    public static GroupKeyResponse toRes(Group group) {
        return GroupKeyResponse.builder()
                .key(group.getKey())
                .build();
    }
}
