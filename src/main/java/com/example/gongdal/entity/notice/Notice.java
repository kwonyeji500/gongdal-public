package com.example.gongdal.entity.notice;

import com.example.gongdal.entity.BaseEntity;
import com.example.gongdal.entity.user.User;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_notice_bas")
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "active")
    private boolean active;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "type")
    private NoticeType type;

    @Column(name = "writer_id")
    private Long writerId;

    public static Notice create(String message, User user, Long scheduleId, NoticeType type, Long writerId) {
        return Notice.builder()
                .message(message)
                .active(false)
                .user(user)
                .scheduleId(scheduleId)
                .type(type)
                .writerId(writerId)
                .build();
    }

    public void updateActive() {
        this.active = true;
    }
}
