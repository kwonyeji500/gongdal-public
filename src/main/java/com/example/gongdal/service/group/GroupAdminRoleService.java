package com.example.gongdal.service.group;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.group.adapter.GroupAdapter;
import com.example.gongdal.dto.group.command.GroupCommissionCommand;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupAdminRoleService {
    private final GroupAdapter groupAdapter;
    private final GroupValidator groupValidator;

    @Transactional
    public void commissionLeader(GroupCommissionCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());

        validCommission(command, group);

        User target = group.getUsers().stream()
                .filter(user -> user.getId().equals(command.getTargetId()))
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP_USER));

        group.updateLeader(target);
    }

    private void validCommission(GroupCommissionCommand command, Group group) {
        if (!groupValidator.validLeader(command.getUser(), group)) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_GROUP);
        }

        if (command.getUser().getId() == command.getTargetId()) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_VALID_ROLE);
        }
    }

    @Transactional
    public void commissionSubLeader(GroupCommissionCommand command) {
        Group group = groupAdapter.validGroup(command.getGroupId());

        validCommission(command, group);

        User target = group.getUsers().stream()
                .filter(user -> user.getId().equals(command.getTargetId()))
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP_USER));

        if (groupValidator.validSubLeader(group.getSubLeaders(), target.getId())) {
            group.exitSubLeader(target);
        } else {
            group.addSubLeader(target);
        }
    }
}
