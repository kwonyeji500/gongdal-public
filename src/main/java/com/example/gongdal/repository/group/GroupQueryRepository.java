package com.example.gongdal.repository.group;

import com.example.gongdal.dto.group.GroupMemberListQueryDto;
import com.example.gongdal.dto.group.command.GroupMemberListCommand;

public interface GroupQueryRepository {
    GroupMemberListQueryDto getMemberList(GroupMemberListCommand command);
}
