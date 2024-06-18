package com.example.gongdal.controller.group.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GroupInfoChangeRequest {
    private String name;
    private String color;
    private String description;
    private String password;
}
