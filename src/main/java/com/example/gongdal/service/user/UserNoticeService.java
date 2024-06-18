package com.example.gongdal.service.user;

import com.example.gongdal.controller.user.adapter.UserAdapter;
import com.example.gongdal.dto.user.command.UserNoticeUpdateCommand;
import com.example.gongdal.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserNoticeService {
    private final UserAdapter userAdapter;

    @Transactional
    public void updateNotice(UserNoticeUpdateCommand command) {
        User user = userAdapter.validUserId(command.getUser().getId());

        user.updateNotice(command.isNotice());
    }
}
