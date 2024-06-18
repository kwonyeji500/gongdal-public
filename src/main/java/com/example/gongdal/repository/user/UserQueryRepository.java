package com.example.gongdal.repository.user;


import com.example.gongdal.dto.user.command.UserGroupInfoCommand;
import com.example.gongdal.entity.group.Group;

import java.util.List;

public interface UserQueryRepository {
    List<Group> getUserGroupList(UserGroupInfoCommand command);
}
