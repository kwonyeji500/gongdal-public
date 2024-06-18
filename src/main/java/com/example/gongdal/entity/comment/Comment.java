package com.example.gongdal.entity.comment;

import com.example.gongdal.entity.BaseEntity;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_comment_bas")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public static Comment createComment(Schedule schedule, User user, String content) {
        return Comment.builder()
                .content(content)
                .user(user)
                .schedule(schedule)
                .build();
    }

    public static Comment createReplyComment(Schedule schedule, Comment comment, User user, String content) {
        return Comment.builder()
                .content(content)
                .parent(comment)
                .user(user)
                .schedule(schedule)
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
