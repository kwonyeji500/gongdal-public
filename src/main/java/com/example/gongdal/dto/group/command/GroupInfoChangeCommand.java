package com.example.gongdal.dto.group.command;

import com.example.gongdal.controller.group.request.GroupInfoChangeRequest;
import com.example.gongdal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
public class GroupInfoChangeCommand {
    private String name;
    private String color;
    private String description;
    private String password;
    private Long groupId;
    private User user;
    private MultipartFile cover;

    public static GroupInfoChangeCommand toCommand(GroupInfoChangeRequest request, Long groupId, User user, MultipartFile file) {
        return GroupInfoChangeCommand.builder()
                .name(request.getName())
                .color(request.getColor())
                .description(request.getDescription())
                .password(request.getPassword())
                .groupId(groupId)
                .user(user)
                .cover(file)
                .build();
    }
}
