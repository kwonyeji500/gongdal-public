package com.example.gongdal.controller.schedule.adapter;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.repository.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ScheduleAdapter {
    private final ScheduleRepository scheduleRepository;

    public Schedule validSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_SCHEDULE));
    }

    public void checkUserSchedule(User user, Schedule schedule) {
        if (user.getId() != schedule.getUser().getId()) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_SCHEDULE);
        }
    }

    public void checkGroupSchedule(User user, Schedule schedule) {
        Group group = schedule.getGroup();
        Long userId = user.getId();

        boolean isLeader = group.getLeader().getId() == userId;
        boolean isWriter = schedule.getWriter().getId() == userId;
        boolean isSubLeader = group.getSubLeaders().stream().anyMatch(u -> u.getId().equals(userId));

        if (!isLeader && !isWriter && !isSubLeader) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_SCHEDULE);
        }
    }

}
