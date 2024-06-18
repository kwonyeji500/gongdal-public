package com.example.gongdal.dto.user;

import com.example.gongdal.entity.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserInfoDto {
    private Long userId;
    private String nickname;
    private String loginId;
    private LocalDate birth;
    private byte[] profile;
    private String role;

    public static UserInfoDto toDto(User user, byte[] profile, String role) {
        return UserInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .loginId(user.getLoginId())
                .birth(user.getBirth())
                .profile(profile)
                .role(role)
                .build();
    }
}
