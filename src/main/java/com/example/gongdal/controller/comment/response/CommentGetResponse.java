package com.example.gongdal.controller.comment.response;


import com.example.gongdal.dto.comment.CommentGetDto;
import com.example.gongdal.entity.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Getter
@Setter
public class CommentGetResponse {
    private Long commentId;
    private String content;
    private Long userId;
    private String nickname;
    private int childrenCount;
    private List<CommentGetResponse> children;
    private String createDate;
    private Long parentId;
    private boolean editable;
    private boolean deletable;

    public static CommentGetResponse toRes(Comment comment) {
        return CommentGetResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .childrenCount(comment.getChildren() != null ? comment.getChildren().size() : 0)
                .children(comment.getChildren() != null ?
                        comment.getChildren().stream().limit(2).map(CommentGetResponse::toChildren).toList() : null)
                .createDate(comment.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .build();
    }

    public static CommentGetResponse toChildren(Comment comment) {
        return CommentGetResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .parentId(comment.getParent().getId())
                .createDate(comment.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .build();
    }

    public static CommentGetResponse toCreateRes(Comment comment) {
        return CommentGetResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .childrenCount(0)
                .children(null)
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .createDate(comment.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .editable(true)
                .deletable(true)
                .build();
    }
}
