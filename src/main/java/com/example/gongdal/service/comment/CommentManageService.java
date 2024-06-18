package com.example.gongdal.service.comment;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.comment.adapter.CommentAdapter;
import com.example.gongdal.controller.schedule.adapter.ScheduleAdapter;
import com.example.gongdal.dto.comment.CommentCreateCommand;
import com.example.gongdal.dto.comment.CommentDeleteCommand;
import com.example.gongdal.dto.comment.CommentUpdateCommand;
import com.example.gongdal.entity.comment.Comment;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.repository.comment.CommentRepository;
import com.example.gongdal.service.group.GroupValidator;
import com.example.gongdal.service.notice.NoticeCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentManageService {
    private final CommentRepository commentRepository;
    private final ScheduleAdapter scheduleAdapter;
    private final GroupValidator groupValidator;
    private final CommentAdapter commentAdapter;
    private final CommentValidator commentValidator;
    private final NoticeCreateService noticeCreateService;


    public Comment createComment(CommentCreateCommand command) {
        Schedule schedule = scheduleAdapter.validSchedule(command.getScheduleId());

        if (schedule.getGroup() != null) {
            if (!groupValidator.validGroupMember(schedule.getGroup().getUsers(), command.getUser().getId())) {
                throw new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP_USER);
            }
        } else {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_WRITE_PERSONAL);
        }

        if (command.getParentId() != null) {
            Comment comment = commentAdapter.validComment(command.getParentId());
            if (comment.getParent() != null) {
                throw new CustomRuntimeException(ErrorResponseCode.NOT_WRITE_REPLY);
            }
            noticeCreateService.create(schedule, command.getUser(), comment);
            return commentRepository.save(Comment.createReplyComment(schedule, comment, command.getUser(), command.getContent()));
        } else {
            noticeCreateService.create(schedule, command.getUser());
            return commentRepository.save(Comment.createComment(schedule, command.getUser(), command.getContent()));
        }
    }

    @Transactional
    public void updateComment(CommentUpdateCommand command) {
        Comment comment = commentAdapter.validComment(command.getCommentId());

        if (!commentValidator.validWriter(command.getUser().getId(), comment)) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_COMMENT);
        }

        comment.updateContent(command.getContent());
    }

    public void deleteComment(CommentDeleteCommand command) {
        Comment comment = commentAdapter.validComment(command.getCommentId());

        if (!commentValidator.validWriter(command.getUser().getId(), comment)) {
            if (!groupValidator.validAdmin(comment.getSchedule().getGroup(), command.getUser().getId())) {
                throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_COMMENT);
            }
        }

        commentRepository.delete(comment);
    }
}
