package com.example.gongdal.controller.comment.adapter;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.entity.comment.Comment;
import com.example.gongdal.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAdapter {
    private final CommentRepository commentRepository;

    public Comment validComment(Long parentId) {
        return commentRepository.findById(parentId).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_COMMENT));
    }
}
