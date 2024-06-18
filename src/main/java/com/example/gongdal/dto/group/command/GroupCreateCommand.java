package com.example.gongdal.dto.group.command;

import com.example.gongdal.controller.group.request.GroupCreateRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
public class GroupCreateCommand {
    private User user;
    private String name;
    private String description;
    private String color;
    private String password;
    private MultipartFile cover;

    public static GroupCreateCommand toCommand(User user, GroupCreateRequest request, MultipartFile file) {
        return GroupCreateCommand.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .color(request.getColor())
                .password(request.getPassword())
                .cover(file)
                .build();
    }
}
