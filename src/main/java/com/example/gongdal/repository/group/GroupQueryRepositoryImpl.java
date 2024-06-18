package com.example.gongdal.repository.group;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.dto.group.GroupMemberListQueryDto;
import com.example.gongdal.dto.group.command.GroupMemberListCommand;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.QUser;
import com.example.gongdal.entity.user.User;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.gongdal.entity.group.QGroup.group;
import static com.example.gongdal.entity.user.QUser.user;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupQueryRepositoryImpl implements GroupQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public GroupMemberListQueryDto getMemberList(GroupMemberListCommand command) {
        Group groups = queryFactory.selectFrom(group)
                .leftJoin(group.users, user).fetchJoin()
                .where(group.id.eq(command.getGroupId()))
                .fetchOne();

        if (groups == null) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_GROUP);
        }

// 사용자 목록을 페이지네이션하여 가져옴
        List<User> users = queryFactory.selectFrom(QUser.user)
                .join(user.groups, group)
                .where(group.id.eq(command.getGroupId())
                        .and(nicknameContains(command.getNickname())))
                .offset(command.getPageable().getOffset())
                .limit(command.getPageable().getPageSize())
                .fetch();

// 전체 사용자 수를 가져옴
        long total = queryFactory.selectFrom(QUser.user)
                .join(user.groups, group)
                .where(group.id.eq(command.getGroupId())
                        .and(nicknameContains(command.getNickname())))
                .fetchCount();

        List<User> sortedUsers = orderUser(groups, users);


        Page<User> userPage = new PageImpl<>(sortedUsers, command.getPageable(), total);

        return GroupMemberListQueryDto.toDto(groups, userPage);
    }



    private static List<User> orderUser(Group group, List<User> users) {
        User leader = group.getLeader();
        List<User> subLeaders = group.getSubLeaders();

        List<User> sortedUsers = new ArrayList<>();
        if (leader != null && users.contains(leader)) {
            sortedUsers.add(leader);
        }
        for (User subLeader : subLeaders) {
            if (users.contains(subLeader)) {
                sortedUsers.add(subLeader);
            }
            users.removeAll(sortedUsers);
            sortedUsers.addAll(users);
        }
        return sortedUsers;
    }


    private Predicate nicknameContains(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return null;
        }
        return user.nickname.containsIgnoreCase(nickname);
    }
}
