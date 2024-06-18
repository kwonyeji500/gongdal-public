package com.example.gongdal.entity.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NoticeType {
    S("schedule", "일정"),
    C("comment", "댓글"),
    R("reply", "대댓글");

    private final String name;
    private final String description;
}
