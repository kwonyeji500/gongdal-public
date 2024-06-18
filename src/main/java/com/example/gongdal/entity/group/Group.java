package com.example.gongdal.entity.group;

import com.example.gongdal.dto.group.command.GroupCreateCommand;
import com.example.gongdal.entity.BaseEntity;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_group_bas")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "group_key")
    private String key;

    @Column(name = "color")
    private String color;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leader_id")
    private User leader;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_sub_leader_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> subLeaders = new ArrayList<>();

    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "cover_id")
    private File cover;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    public void addUser(User user) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(user);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public static Group create(String key, GroupCreateCommand command, String password, User user) {
        Group group = Group.builder()
                .name(command.getName())
                .password(password)
                .key(key)
                .color(command.getColor())
                .description(command.getDescription())
                .leader(user)
                .build();

        group.addUser(user);
        return group;
    }

    public void updateCover(File file) {
        this.cover = file;
    }

    public void deleteCover() {
        this.cover = null;
    }

    public void updateLeader(User user) {
        this.leader = user;
    }

    public void exitSubLeader(User user) {
        this.subLeaders.remove(user);
    }

    public void addSubLeader(User user) {
        if (this.subLeaders == null) {
            this.subLeaders = new ArrayList<>();
        }
        this.subLeaders.add(user);
    }
}
