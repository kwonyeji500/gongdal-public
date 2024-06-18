package com.example.gongdal.service.group;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.group.adapter.GroupAdapter;
import com.example.gongdal.controller.user.adapter.UserAdapter;
import com.example.gongdal.dto.group.*;
import com.example.gongdal.dto.group.command.GroupAdminInfoCommand;
import com.example.gongdal.dto.group.command.GroupDeleteCommand;
import com.example.gongdal.dto.group.command.GroupInfoChangeCommand;
import com.example.gongdal.dto.group.command.GroupKickCommand;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.repository.group.GroupRepository;
import com.example.gongdal.repository.user.UserRepository;
import com.example.gongdal.service.fcm.FcmSubscriptionManager;
import com.example.gongdal.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupAdminService {
    private final GroupAdapter groupAdapter;
    private final PasswordEncoder passwordEncoder;
    private final UserAdapter userAdapter;
    private final FileStorageService fileStorageService;
    private final GroupValidator groupValidator;
    private final FcmSubscriptionManager fcmSubscriptionManager;


    @Transactional
    public void changeInfo(GroupInfoChangeCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());
        Long userId = command.getUser().getId();

        if (!groupValidator.validAdmin(group, userId)) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_GROUP);
        }

        Optional.ofNullable(command.getName()).ifPresent(group::updateName);
        Optional.ofNullable(command.getDescription()).ifPresent(group::updateDescription);
        Optional.ofNullable(command.getColor()).ifPresent(group::updateColor);

        if (command.getPassword() != null) {
            group.updatePassword(passwordEncoder.encode(command.getPassword()));
        }

        if (command.getCover() != null) {
            if (group.getCover() != null) {
                fileStorageService.deleteFile(group.getCover());
            }
            File file = fileStorageService.saveFile(command.getCover());
            group.updateCover(file);
        }
    }

    @Transactional
    public void kickMember(GroupKickCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());

        if(!groupValidator.validAdmin(group, command.getUser().getId())) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_GROUP);
        }

        groupValidator.validKick(group, command.getUser(), command.getKickUserId());

        User kickUser = userAdapter.validUserId(command.getKickUserId());

        kickUser.exitGroup(group);

        fcmSubscriptionManager.unsubscribeFromTopics(kickUser.getFcmToken(), List.of(group.getKey()));
    }
}
