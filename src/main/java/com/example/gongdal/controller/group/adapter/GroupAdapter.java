package com.example.gongdal.controller.group.adapter;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.repository.group.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupAdapter {
    private final GroupRepository groupRepository;

    public Group validGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP));
    }

    public Group validGroupKey(String key) {
        return groupRepository.findByKey(key).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP)
        );
    }

    public Group validLeader(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP));

        if (group.getLeader().getId() != userId) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_GROUP);
        }

        return group;
    }
}
