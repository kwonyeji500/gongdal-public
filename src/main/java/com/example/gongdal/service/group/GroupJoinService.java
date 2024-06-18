package com.example.gongdal.service.group;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.group.adapter.GroupAdapter;
import com.example.gongdal.controller.user.adapter.UserAdapter;
import com.example.gongdal.dto.group.command.GroupCreateCommand;
import com.example.gongdal.dto.group.command.GroupExitCommand;
import com.example.gongdal.dto.group.command.GroupJoinCommand;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.repository.group.GroupRepository;
import com.example.gongdal.service.fcm.FcmSubscriptionManager;
import com.example.gongdal.service.file.FileStorageService;
import com.example.gongdal.util.RandomGroupKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupJoinService {

    private final RandomGroupKeyGenerator keyGenerator;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final UserAdapter userAdapter;
    private final GroupAdapter groupAdapter;
    private final FileStorageService fileStorageService;
    private final GroupValidator groupValidator;
    private final FcmSubscriptionManager fcmSubscriptionManager;

    @Transactional
    public Group createGroup(GroupCreateCommand command) {
        String key = keyGenerator.generateUniqueKey();

        String password = passwordEncoder.encode(command.getPassword());

        User user = userAdapter.validUserId(command.getUser().getId());

        if (user.getLeaderGroups() != null && user.getLeaderGroups().stream().count() > 3) {
            throw new CustomRuntimeException(ErrorResponseCode.GROUP_CREATE_RANGE_OVER);
        }

        Group group = Group.create(key, command, password, user);

        if (command.getCover() != null) {
            File file = fileStorageService.saveFile(command.getCover());
            group.updateCover(file);
        }

        group = groupRepository.save(group);

        user.addGroup(group);
        fcmSubscriptionManager.subscribeToTopics(user.getFcmToken(), List.of(group.getKey()));

        return group;
    }

    @Transactional
    public void joinGroup(GroupJoinCommand command) {
        User user = userAdapter.validUserId(command.getUser().getId());
        groupValidator.validGroup(user, command.getGroupId());

        if (user.getGroups() != null && user.getGroups().stream().count() > 10) {
            throw new CustomRuntimeException(ErrorResponseCode.GROUP_JOIN_RANGE_OVER);
        }

        Group group = groupAdapter.validGroup(command.getGroupId());
        log.info("[GroupJoinService] joinGroup - groupId : {}, userId : {}", group.getId(), user.getId());

        if (group.getUsers() != null && group.getUsers().stream().count() > 30) {
            throw new CustomRuntimeException(ErrorResponseCode.GROUP_COUNT_RANGE_OVER);
        }

        if (!passwordEncoder.matches(command.getPassword(), group.getPassword())) {
            throw new CustomRuntimeException(ErrorResponseCode.GROUP_LOGIN_ERROR);
        }

        user.addGroup(group);

        fcmSubscriptionManager.subscribeToTopics(user.getFcmToken(), List.of(group.getKey()));
    }

    @Transactional
    public void exitGroup(GroupExitCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());
        User user = userAdapter.validUserId(command.getUser().getId());
        log.info("[GroupJoinService] exitGroup - groupId : {}, userId : {}", group.getId(), user.getId());

        if (!groupValidator.validGroupMember(group.getUsers(), user.getId())) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP_USER);
        }

        if (group.getLeader().getId().equals(user.getId())) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_EXIT_LEADER);
        }

        user.exitGroup(group);

        fcmSubscriptionManager.unsubscribeFromTopics(user.getFcmToken(), List.of(group.getKey()));
    }
}
