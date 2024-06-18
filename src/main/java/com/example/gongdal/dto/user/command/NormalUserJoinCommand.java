package com.example.gongdal.dto.user.command;

import com.example.gongdal.controller.user.request.NormalUserJoinRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class NormalUserJoinCommand {
    private String loginId;
    private String password;

    public static NormalUserJoinCommand toCommand(NormalUserJoinRequest request) {
        return NormalUserJoinCommand.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .build();
    }
}
