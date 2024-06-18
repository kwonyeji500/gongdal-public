package com.example.gongdal.controller.group.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class GroupKickRequest {
    @NotBlank
    private Long groupId;
    @NotBlank
    private Long kickUserId;
}
