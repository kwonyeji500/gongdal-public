package com.example.gongdal.dto.user.command;

import com.example.gongdal.controller.user.request.UserUpdateRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class UserUpdateCommand {
    private User user;
    private LocalDate birth;
    private String nickname;
    private MultipartFile profile;

    public static UserUpdateCommand toCommand(User user, UserUpdateRequest request, MultipartFile file) {
        return UserUpdateCommand.builder()
                .user(user)
                .birth(request.getBirth())
                .nickname(request.getNickname())
                .profile(file)
                .build();
    }
}
