package com.example.gongdal.entity.schedule;

import com.example.gongdal.dto.schedule.command.CreateScheduleCommand;
import com.example.gongdal.entity.BaseEntity;
import com.example.gongdal.entity.comment.Comment;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.notice.Notice;
import com.example.gongdal.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_schedule_bas")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "corrector_id")
    private User corrector;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private File file;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public static Schedule createUser(CreateScheduleCommand command, File file) {
        return Schedule.builder()
                .name(command.getName())
                .description(command.getDescription())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .user(command.getUser())
                .file(file)
                .build();
    }

    public static Schedule createGroup(CreateScheduleCommand command, Group group, File file) {
        return Schedule.builder()
                .name(command.getName())
                .description(command.getDescription())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .group(group)
                .writer(command.getUser())
                .file(file)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateStart(LocalDateTime start) {
        this.startDate = start;
    }

    public void updateEnd(LocalDateTime end) {
        this.endDate = end;
    }

    public void updateCorrector(User user) {
        this.corrector = user;
    }

    public void updateFile(File file) {
        this.file = file;
    }

    public void deleteFile() {
        this.file = null;
    }
}
