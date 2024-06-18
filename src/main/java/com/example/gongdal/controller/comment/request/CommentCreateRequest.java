package com.example.gongdal.controller.comment.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class CommentCreateRequest {
    @NotBlank
    private String content;
    private Long parentId;
}
