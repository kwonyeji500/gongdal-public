package com.example.gongdal.service.group;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.group.adapter.GroupAdapter;
import com.example.gongdal.dto.group.GroupAdminInfoDto;
import com.example.gongdal.dto.group.command.GroupAdminInfoCommand;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupAdminSearchService {
    private final GroupAdapter groupAdapter;
    private final GroupValidator groupValidator;
    private final FileStorageService fileStorageService;

    public GroupAdminInfoDto getAdminInfo(GroupAdminInfoCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());

        if (!groupValidator.validAdmin(group, command.getUser().getId())) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_GROUP);
        }

        if(group.getCover() != null) {
            byte[] cover = fileStorageService.downloadFile(group.getCover().getId());
            return GroupAdminInfoDto.toCoverDto(group, cover);
        }
        return GroupAdminInfoDto.toDto(group);
    }
}
