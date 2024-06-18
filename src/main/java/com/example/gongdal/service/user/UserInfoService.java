package com.example.gongdal.service.user;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.file.adapter.FileAdapter;
import com.example.gongdal.controller.user.adapter.UserAdapter;
import com.example.gongdal.controller.user.response.UserInfoResponse;
import com.example.gongdal.dto.user.command.UserDeleteProfileCommand;
import com.example.gongdal.dto.user.command.UserGroupInfoCommand;
import com.example.gongdal.dto.user.UserGroupInfoDto;
import com.example.gongdal.dto.user.command.UserInfoCommand;
import com.example.gongdal.dto.user.command.UserUpdateCommand;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.repository.user.UserQueryRepository;
import com.example.gongdal.repository.user.UserRepository;
import com.example.gongdal.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final FileStorageService fileStorageService;
    private final UserAdapter userAdapter;
    private final FileAdapter fileAdapter;

    @Transactional
    public void updateUser(UserUpdateCommand command) {
        User user = userAdapter.validUserId(command.getUser().getId());

        if (command.getBirth() != null) {
            user.updateBirth(command.getBirth());
        }

        if (command.getNickname() != null) {
            user.updateNickname(command.getNickname());
        }

        if (command.getProfile() != null) {
            if(command.getUser().getProfile() != null) {
                fileStorageService.deleteFile(command.getUser().getProfile());
            }

            File profile = fileStorageService.saveFile(command.getProfile());

            user.updateProfile(profile);
        }
    }

    public List<UserGroupInfoDto> getUserGroup(UserGroupInfoCommand command) {
        List<Group> groups = userQueryRepository.getUserGroupList(command);
        List<UserGroupInfoDto> dtoList = new ArrayList<>();

        for (Group group : groups) {
            byte[] cover = new byte[0];
            byte[] leaderProfile = new byte[0];
            if (group.getCover() != null) {
                cover = fileStorageService.downloadFile(group.getCover().getId());
            }

            if (group.getLeader().getProfile() != null) {
                leaderProfile = fileStorageService.downloadFile(group.getLeader().getProfile().getId());
            }

            dtoList.add(UserGroupInfoDto.toDto(group, cover, leaderProfile));
        }
        return dtoList;
    }

    public UserInfoResponse getUserInfo(UserInfoCommand command) {
        if (command.getUser().getProfile() != null) {
        byte[] profile = fileStorageService.downloadFile(command.getUser().getProfile().getId());
        return UserInfoResponse.toRes(command.getUser(), profile);
        }
        return UserInfoResponse.toRes(command.getUser(), null);
    }

    public void deleteProfile(UserDeleteProfileCommand command) {
        if (command.getUser().getProfile() == null) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_FILE);
        }

        File file = fileAdapter.validFile(command.getUser().getProfile().getId());

        command.getUser().deleteProfile();
        userRepository.save(command.getUser());
        fileStorageService.deleteFile(file);
    }
}
