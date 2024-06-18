package com.example.gongdal.repository.user;


import com.example.gongdal.dto.user.command.UserGroupInfoCommand;
import com.example.gongdal.entity.group.Group;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.gongdal.entity.group.QGroup.group;
import static com.example.gongdal.entity.user.QUser.user;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Group> getUserGroupList(UserGroupInfoCommand command) {

        return queryFactory.selectFrom(group)
                .join(group.users, user)
                .join(group.leader).fetchJoin()
                .where(user.id.eq(command.getUser().getId()))
                .fetch();
    }
}
