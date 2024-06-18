package com.example.gongdal.controller.group.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GroupCommissionRequest {
    private Long groupId;
    private Long targetId;
}
