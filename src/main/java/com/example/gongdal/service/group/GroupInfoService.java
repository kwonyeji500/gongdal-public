package com.example.gongdal.service.group;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.group.adapter.GroupAdapter;
import com.example.gongdal.dto.group.GroupMemberListDto;
import com.example.gongdal.dto.group.GroupMemberListQueryDto;
import com.example.gongdal.dto.group.GroupSearchInfoDto;
import com.example.gongdal.dto.group.command.GroupGetFeedCommand;
import com.example.gongdal.dto.group.command.GroupKeyCommand;
import com.example.gongdal.dto.group.command.GroupMemberListCommand;
import com.example.gongdal.dto.group.command.GroupSearchInfoCommand;
import com.example.gongdal.dto.user.UserInfoDto;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.repository.group.GroupQueryRepository;
import com.example.gongdal.repository.group.GroupRepository;
import com.example.gongdal.repository.schedule.ScheduleRepository;
import com.example.gongdal.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupInfoService {
    private final GroupAdapter groupAdapter;
    private final FileStorageService fileStorageService;
    private final GroupQueryRepository groupQueryRepository;
    private final GroupValidator groupValidator;
    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;

    public Group getKey(GroupKeyCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());

        group.getUsers().stream()
                .filter(user -> user.getId().equals(command.getUser().getId()))
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP_USER));

        return group;
    }

    public GroupSearchInfoDto getInfo(GroupSearchInfoCommand command) {
        Group group = groupAdapter.validGroupKey(command.getKey());

        if (group.getCover() != null) {
            byte[] cover = fileStorageService.downloadFile(group.getCover().getId());
            return GroupSearchInfoDto.toCoverDto(group, cover);
        }
        return GroupSearchInfoDto.toDto(group);
    }

    public GroupMemberListDto getMemberList(GroupMemberListCommand command) {
        GroupMemberListQueryDto dto = groupQueryRepository.getMemberList(command);

        Group group = dto.getGroup();
        if (!groupValidator.validGroupMember(group.getUsers(), command.getUser().getId())) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_GROUP);
        }

        boolean auth = group.getLeader().getId() == command.getUser().getId();
        boolean kick = groupValidator.validAdmin(group, command.getUser().getId());

        Page<User> users = groupRepository.findUsersByGroupId(group.getId(), command.getPageable());

        Page<UserInfoDto> userInfoDtoPage = getUserInfoDtos(users, group);

        return GroupMemberListDto.toDto(kick, auth, userInfoDtoPage);
    }

    private Page<UserInfoDto> getUserInfoDtos(Page<User> users, Group group) {
        List<UserInfoDto> userInfoDtoList = users.stream().map(user -> {
            byte[] profile = null;
            String role = "";

            if (user.getProfile() != null) {
                profile = fileStorageService.downloadFile(user.getProfile().getId());
            }
            if (groupValidator.validLeader(user, group)) {
                role = "leader";
            } else if (groupValidator.validSubLeader(group.getSubLeaders(), user.getId())) {
                role = "subLeader";
            } else {
                role = "member";
            }

            return UserInfoDto.toDto(user, profile, role);
        }).collect(Collectors.toList());

        // 역할(role) 기준으로 정렬
        userInfoDtoList.sort(Comparator.comparing(UserInfoDto::getRole, Comparator.comparingInt(role -> {
            switch (role) {
                case "leader":
                    return 0;
                case "subLeader":
                    return 1;
                case "member":
                    return 2;
                default:
                    return 3;
            }
        })));

        Pageable pageable = users.getPageable();
        long total = users.getTotalElements();

        return new PageImpl<>(userInfoDtoList, pageable, total);
    }

    public Page<Schedule> getGroupFeed(GroupGetFeedCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());

        if (!groupValidator.validGroupMember(group.getUsers(), command.getUser().getId())) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP_USER);
        }

        Pageable pageable = command.getPageable();

        return scheduleRepository.findByGroup(group, pageable);
    }
}
