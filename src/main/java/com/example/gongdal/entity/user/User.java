package com.example.gongdal.entity.user;

import com.example.gongdal.dto.token.TokenCommand;
import com.example.gongdal.dto.user.command.NormalUserJoinCommand;
import com.example.gongdal.dto.user.command.SocialCheckCommand;
import com.example.gongdal.entity.BaseEntity;
import com.example.gongdal.entity.comment.Comment;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.notice.Notice;
import com.example.gongdal.entity.schedule.Schedule;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_user_bas")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "nick_name")
    private String nickname;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "join_type")
    @Enumerated(EnumType.STRING)
    private UserType joinType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private File profile;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notice> notices = new ArrayList<>();

    @ManyToMany(mappedBy = "subLeaders", fetch = FetchType.LAZY)
    private List<Group> subLeaderGroup;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "leader", fetch = FetchType.LAZY)
    private List<Group> leaderGroups = new ArrayList<>();

    @Column(name = "notice", nullable = false)
    private boolean notice = false;

    public static User createNormal(NormalUserJoinCommand command, String password, String nickName) {
        return User.builder()
                .loginId(command.getLoginId())
                .password(password)
                .nickname(nickName)
                .joinType(UserType.N)
                .status(UserStatus.ACTIVE)
                .notice(false)
                .build();
    }

    public static User createSocial(SocialCheckCommand command, String loginId, String nickName) {
        return User.builder()
                .loginId(loginId)
                .fcmToken(command.getFcmToken())
                .nickname(nickName)
                .joinType(command.getType())
                .status(UserStatus.ACTIVE)
                .notice(false)
                .build();
    }

    public void login(String access, String refresh) {
        this.accessToken = access;
        this.refreshToken = refresh;
    }

    public void logout() {
        this.accessToken = null;
        this.refreshToken = null;
    }

    public void renewToken(TokenCommand command) {
        this.accessToken = command.getAccessToken();
        this.refreshToken = command.getRefreshToken();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateBirth(LocalDate birth) {
        this.birth = birth;
    }

    public void updateProfile(File profile) {
        this.profile = profile;
    }

    public void addGroup(Group group) {
        if (this.groups == null) {
            this.groups = new ArrayList<>();
        }
        this.groups.add(group);
    }

    public void exitGroup(Group group) {
        this.groups.remove(group);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void deleteProfile() {
        this.profile = null;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void updateNotice(boolean notice) {
        this.notice = notice;
    }
}
