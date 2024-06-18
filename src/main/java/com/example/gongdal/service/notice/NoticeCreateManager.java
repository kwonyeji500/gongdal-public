package com.example.gongdal.service.notice;

import com.example.gongdal.dto.fcm.FcmSendDto;
import com.example.gongdal.entity.comment.Comment;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.notice.Notice;
import com.example.gongdal.entity.notice.NoticeType;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.repository.notice.NoticeRepository;
import com.example.gongdal.service.fcm.FcmSendManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeCreateService {
    private final NoticeRepository noticeRepository;
    private final FcmSendManager fcmSendManager;
    @Value("${fcm.topic}")
    private String topicName;
    private static final String SCHEDULE_CREATED = "%s님이 %s의 일정을 생성하였습니다.";
    private static final String COMMENT_CREATED = "%s님이 %s 일정에 댓글을 생성하였습니다.";
    private static final String CHILDREN_CREATED = "%s님이 내 댓글에 댓글을 남기셨습니다.";

    public void create(Group group, User user, Long scheduleId) {
        List<User> sendUser = group.getUsers().stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .toList();
        String message = String.format(SCHEDULE_CREATED, user.getNickname(), group.getName());

        List<Notice> sendNotice = new ArrayList<>();
        for (User u : sendUser) {
            sendNotice.add(Notice.create(message, u, scheduleId, NoticeType.S, user.getId()));
        }
        noticeRepository.saveAll(sendNotice);
        fcmSendManager.sendMessageTo(FcmSendDto.toSchedule(group.getKey() + topicName, message));
    }

    public void create(Schedule schedule, User user) {
        String message = String.format(COMMENT_CREATED, user.getNickname(), schedule.getName());
        User sendUser = schedule.getGroup() == null ? schedule.getUser() : schedule.getWriter();

        if (sendUser.getId() == user.getId()) {
            return;
        }

        noticeRepository.save(Notice.create(message, sendUser, schedule.getId(), NoticeType.C, user.getId()));
        if (sendUser.isNotice()) {
            fcmSendManager.sendMessageTo(FcmSendDto.toComment(sendUser.getFcmToken(), message));
        }
    }

    public void create(Schedule schedule, User user, Comment comment) {
        if (comment.getUser().getId() == user.getId()) {
            return;
        }
        String message = String.format(CHILDREN_CREATED, user.getNickname());

        noticeRepository.save(Notice.create(message, comment.getUser(), schedule.getId(), NoticeType.R, user.getId()));
        if (comment.getUser().isNotice()) {
            fcmSendManager.sendMessageTo(FcmSendDto.toComment(comment.getUser().getFcmToken(), message));
        }
    }
}
