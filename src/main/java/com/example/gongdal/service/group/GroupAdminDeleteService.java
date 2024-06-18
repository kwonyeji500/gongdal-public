package com.example.gongdal.service.group;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.group.adapter.GroupAdapter;
import com.example.gongdal.dto.group.command.GroupDeleteCommand;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.repository.group.GroupRepository;
import com.example.gongdal.repository.user.UserRepository;
import com.example.gongdal.service.fcm.FcmSubscriptionManager;
import com.example.gongdal.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupAdminDeleteService {
    private final GroupAdapter groupAdapter;
    private final FileStorageService fileStorageService;
    private final GroupValidator groupValidator;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FcmSubscriptionManager fcmSubscriptionManager;

    @Transactional
    public void deleteGroup(GroupDeleteCommand command) {
        Group group = groupAdapter.validLeader(command.getGroupId(), command.getUser().getId());

        if (group.getCover() != null) {
            fileStorageService.deleteFile(group.getCover());
        }

        List<User> users = new ArrayList<>();
        for (User user : group.getUsers()) {
            user.getGroups().remove(group);
            users.add(user);
        }

        for (User subLeader : group.getSubLeaders()) {
            subLeader.getSubLeaderGroup().remove(group);
            users.add(subLeader);
        }

        userRepository.saveAll(users);
        groupRepository.delete(group);
        fcmSubscriptionManager.unsubscribeFromTokens(group.getUsers().stream().map(User::getFcmToken).toList(), group.getKey());
    }

    @Transactional
    public void deleteCover(GroupDeleteCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());
        File file = group.getCover();

        if (!groupValidator.validAdmin(group, command.getUser().getId())) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_GROUP);
        }

        if (group.getColor() == null) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_FILE);
        }

        group.deleteCover();
        fileStorageService.deleteFile(file);
    }
}
