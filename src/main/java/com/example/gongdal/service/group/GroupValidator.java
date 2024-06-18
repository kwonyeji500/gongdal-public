package com.example.gongdal.service.group;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupValidator {

    public boolean validGroupMember(List<User> users, Long userId) {
        return users.stream()
                .anyMatch(groupUser -> groupUser.getId().equals(userId));
    }

    public void validGroup(User user, Long groupId) {
        if (user.getGroups().stream().anyMatch(group -> group.getId() == groupId)) {
            throw new CustomRuntimeException(ErrorResponseCode.ALREADY_JOIN_GROUP);
        }
    }

    public boolean validAdmin(Group group, Long userId) {
        if (group.getLeader().getId() != userId) {
            return group.getSubLeaders().stream().anyMatch(user -> user.getId().equals(userId));
        }
        return true;
    }

    public void validKick(Group group, User user, Long kickUserId) {
        if (user.getId().equals(kickUserId)) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_VALID_KICK);
        }

        if (group.getLeader().getId().equals(kickUserId)) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_KICK_LEADER);
        }

        if(group.getUsers().stream().noneMatch(u -> u.getId().equals(kickUserId))) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_MATCH_KICK);
        }
    }

    public boolean validLeader(User user, Group group) {
        return group.getLeader().getId() == user.getId();
    }

    public boolean validSubLeader(List<User> subLeaders, Long id) {
        return subLeaders.stream().anyMatch(user -> user.getId() == id);
    }
}
