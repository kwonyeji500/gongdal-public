package com.example.gongdal.controller.user.response;

import com.example.gongdal.entity.user.User;
import com.example.gongdal.entity.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Blob;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponse {
    private String nickname;
    private LocalDate birth;
    private String loginId;
    private Long userId;
    private byte[] profile;
    private boolean notice;

    public static UserInfoResponse toRes(User user, byte[] profile) {
        return UserInfoResponse.builder()
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .loginId(user.getLoginId())
                .userId(user.getId())
                .profile(profile)
                .notice(user.isNotice()).build();
    }
}
