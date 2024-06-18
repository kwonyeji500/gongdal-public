package com.example.gongdal.dto.group.command;

import com.example.gongdal.controller.group.request.GroupSearchInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GroupSearchInfoCommand {
    private String key;

    public static GroupSearchInfoCommand toCommand(GroupSearchInfoRequest request) {
        return GroupSearchInfoCommand.builder()
                .key(request.getKey()).build();
    }
}
