package com.example.gongdal.service.comment;

import com.example.gongdal.entity.comment.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentValidator {

    public boolean validWriter(Long id, Comment command) {
        if (command.getUser().getId() != id) {
            return false;
        }
        return true;
    }
}
