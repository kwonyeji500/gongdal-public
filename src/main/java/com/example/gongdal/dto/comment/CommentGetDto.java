package com.example.gongdal.dto.comment;

import com.example.gongdal.entity.comment.Comment;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class CommentGetDto {
    private Long commentId;
    private String content;
    private Long userId;
    private String nickname;
    private List<CommentGetDto> children;
    private String createDate;
    private boolean editable;
    private boolean deletable;

    public static CommentGetDto toDto(Comment comment) {
        return CommentGetDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .children(comment.getChildren() != null ?
                        comment.getChildren().stream().map(CommentGetDto::toDto).toList() : null)
                .createDate(comment.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .build();
    }

}
