package com.example.gongdal.service.comment;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.comment.response.CommentGetResponse;
import com.example.gongdal.controller.schedule.adapter.ScheduleAdapter;
import com.example.gongdal.dto.comment.CommentGetChildrenCommand;
import com.example.gongdal.dto.comment.CommentGetCommand;
import com.example.gongdal.entity.comment.Comment;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.group.GroupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentSearchService {
    private final ScheduleAdapter scheduleAdapter;
    private final GroupValidator groupValidator;

    public Page<CommentGetResponse> getComments(CommentGetCommand command) {
        Schedule schedule = scheduleAdapter.validSchedule(command.getScheduleId());

        validComment(command.getUser(), schedule);

        List<Comment> comments = schedule.getComments().stream().filter(comment -> comment.getParent() == null).toList();

        return getResponseList(schedule, comments, command.getUser(), command.getPageable());
    }

    public Page<CommentGetResponse> getChildren(CommentGetChildrenCommand command) {
        Schedule schedule = scheduleAdapter.validSchedule(command.getScheduleId());

        validComment(command.getUser(), schedule);

        Comment parent = schedule.getComments().stream().filter(comment -> comment.getId() == command.getCommentId())
                .findFirst().orElseThrow(() -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_COMMENT));

        return getResponseList(schedule, parent.getChildren(), command.getUser(), command.getPageable());
    }

    private PageImpl<CommentGetResponse> getResponseList(Schedule schedule, List<Comment> comments, User requestUser, Pageable pageable) {
        User leader = schedule.getGroup().getLeader();
        List<User> subLeaders = schedule.getGroup().getSubLeaders();

        List<CommentGetResponse> allComments = comments.stream()
                .map(comment -> {
                    CommentGetResponse res = CommentGetResponse.toRes(comment);
                    setEdit(requestUser, comment, leader, subLeaders, res);
                    List<CommentGetResponse> child = comment.getChildren().stream().limit(2).map(children -> {
                        CommentGetResponse ch = CommentGetResponse.toChildren(children);
                        setEdit(requestUser, children, leader, subLeaders, ch);
                        return ch;})
                            .toList();
                    res.setChildren(child);
                    return res;
                }).toList();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<CommentGetResponse> pagedComments;

        if (allComments.size() < startItem) {
            pagedComments = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, allComments.size());
            pagedComments = allComments.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedComments, pageable, allComments.size());
    }

    private static void setEdit(User requestUser, Comment comment, User leader, List<User> subLeaders, CommentGetResponse res) {
        boolean isAuthor = comment.getUser().getId().equals(requestUser.getId());
        boolean isLeaderOrSubLeader = leader.getId().equals(requestUser.getId()) || subLeaders.stream().anyMatch(subLeader -> subLeader.getId().equals(requestUser.getId()));
        res.setEditable(isAuthor);
        res.setDeletable(isAuthor || isLeaderOrSubLeader);
    }

    private void validComment(User user, Schedule schedule) {
        if (schedule.getGroup() == null) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_WRITE_PERSONAL);
        }

        if (!groupValidator.validGroupMember(schedule.getGroup().getUsers(),user.getId())) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP_USER);
        }
    }
}
