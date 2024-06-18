package com.example.gongdal.service.schedule;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.dto.schedule.command.GetScheduleUserCommand;
import com.example.gongdal.controller.schedule.adapter.ScheduleAdapter;
import com.example.gongdal.controller.user.adapter.UserAdapter;
import com.example.gongdal.dto.schedule.GetScheduleDetailDto;
import com.example.gongdal.dto.schedule.command.GetScheduleDetailCommand;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.file.FileStorageService;
import com.example.gongdal.service.group.GroupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleSearchService {
    private final ScheduleAdapter scheduleAdapter;
    private final FileStorageService fileStorageService;
    private final UserAdapter userAdapter;
    private final GroupValidator groupValidator;


    public GetScheduleDetailDto getDetail(GetScheduleDetailCommand command) {
        Schedule schedule = scheduleAdapter.validSchedule(command.getScheduleId());
        checkValid(schedule, command.getUser());

        byte[] file = downFile(schedule.getFile());

        if (schedule.getGroup() == null) {
            return GetScheduleDetailDto.toUserDto(schedule, file, true);
        } else {
            boolean editable = isEditable(schedule, command.getUser());
            return GetScheduleDetailDto.toGroupDto(schedule, file, editable);
        }
    }

    private boolean isEditable(Schedule schedule, User user) {
        if (schedule.getWriter().getId() == user.getId()) {
            return true;
        }
        if (schedule.getGroup() != null) {
            return groupValidator.validAdmin(schedule.getGroup(), user.getId());
        }
        return false;
    }

    private byte[] downFile(File file) {
        if (file != null) {
            return fileStorageService.downloadFile(file.getId());
        }
        return null;
    }

    private void checkValid(Schedule schedule, User user) {
        if (schedule.getGroup() == null) {
            scheduleAdapter.checkUserSchedule(user, schedule);
        } else {
            if (!groupValidator.validGroupMember(schedule.getGroup().getUsers(), user.getId())) {
                throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_SCHEDULE);
            }
        }
    }

    public List<Schedule> getUserSchedule(GetScheduleUserCommand command) {
        User user = userAdapter.validUserId(command.getUser().getId());

        // 필터링된 사용자 일정을 추가합니다
        List<Schedule> scheduleList = new ArrayList<>(user.getSchedules().stream()
                .filter(schedule -> isWithinRange(schedule, command.getStart(), command.getEnd()))
                .toList());

        // 필터링된 그룹 일정을 추가합니다
        for (Group group : user.getGroups()) {
            scheduleList.addAll(group.getSchedules().stream()
                    .filter(schedule -> isWithinRange(schedule, command.getStart(), command.getEnd()))
                    .toList());
        }

        return scheduleList;
    }

    private static boolean isWithinRange(Schedule schedule, LocalDate start, LocalDate end) {
        LocalDate startDate = schedule.getStartDate().toLocalDate();
        LocalDate endDate = schedule.getEndDate().toLocalDate();

        boolean startsBeforeOrOnEnd = startDate.equals(end) || startDate.isBefore(end);
        boolean startsAfterOrOnStart = startDate.equals(start) || startDate.isAfter(start);
        boolean endsAfterOrOnStart = endDate.equals(start) || endDate.isAfter(start);

        return startsBeforeOrOnEnd && startsAfterOrOnStart && endsAfterOrOnStart;
    }
}
