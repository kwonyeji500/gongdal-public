package com.example.gongdal.service.schedule;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.controller.group.adapter.GroupAdapter;
import com.example.gongdal.controller.schedule.adapter.ScheduleAdapter;
import com.example.gongdal.dto.fcm.FcmSendDto;
import com.example.gongdal.dto.schedule.command.CreateScheduleCommand;
import com.example.gongdal.dto.schedule.command.DeleteScheduleCommand;
import com.example.gongdal.dto.schedule.command.UpdateScheduleCommand;
import com.example.gongdal.entity.file.File;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.repository.schedule.ScheduleRepository;
import com.example.gongdal.service.fcm.FcmSendManager;
import com.example.gongdal.service.file.FileStorageService;
import com.example.gongdal.service.group.GroupValidator;
import com.example.gongdal.service.notice.NoticeCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleManageService {
    private final ScheduleRepository scheduleRepository;
    private final GroupAdapter groupAdapter;
    private final ScheduleAdapter scheduleAdapter;
    private final FileStorageService fileStorageService;
    private final GroupValidator groupValidator;
    private final NoticeCreateService noticeCreateService;
    private final FcmSendManager fcmSendManager;

    @Transactional
    public void createSchedule(CreateScheduleCommand command) {
        File file = null;
        if (command.getFile() != null) {
            file = fileStorageService.saveFile(command.getFile());
        }

        if (command.getGroupId() == null) {
            scheduleRepository.save(Schedule.createUser(command, file));
        } else {
            Group group = groupAdapter.validGroup(command.getGroupId());

            if (!groupValidator.validGroupMember(group.getUsers(), command.getUser().getId())) {
                throw new CustomRuntimeException(ErrorResponseCode.NOT_AUTH_GROUP);
            }

            Schedule schedule =scheduleRepository.save(Schedule.createGroup(command, group, file));
            noticeCreateService.create(group, command.getUser(), schedule.getId());
        }
    }

    @Transactional
    public void updateSchedule(UpdateScheduleCommand command) {
        Schedule schedule = scheduleAdapter.validSchedule(command.getScheduleId());

        if (schedule.getGroup() != null) {
            scheduleAdapter.checkGroupSchedule(command.getUser(), schedule);
        } else {
            scheduleAdapter.checkUserSchedule(command.getUser(), schedule);
        }

        if (command.getFile() != null) {
            File file = fileStorageService.saveFile(command.getFile());
            if (schedule.getFile() != null) {
                fileStorageService.deleteFile(schedule.getFile());
            }
            schedule.updateFile(file);
        }

        Optional.ofNullable(command.getName()).ifPresent(schedule::updateName);
        Optional.ofNullable(command.getDescription()).ifPresent(schedule::updateDescription);
        Optional.ofNullable(command.getStartDate()).ifPresent(schedule::updateStart);
        Optional.ofNullable(command.getEndDate()).ifPresent(schedule::updateEnd);
        schedule.updateCorrector(command.getUser());
    }

    public void deleteSchedule(DeleteScheduleCommand command) {
        Schedule schedule = scheduleAdapter.validSchedule(command.getScheduleId());

        if (schedule.getGroup() != null) {
            scheduleAdapter.checkGroupSchedule(command.getUser(), schedule);
        } else {
            scheduleAdapter.checkUserSchedule(command.getUser(), schedule);
        }

        if (schedule.getFile() != null) {
            fileStorageService.deleteFile(schedule.getFile());
        }

        scheduleRepository.delete(schedule);
    }

    public void deleteFile(DeleteScheduleCommand command) {
        Schedule schedule = scheduleAdapter.validSchedule(command.getScheduleId());
        File file = schedule.getFile();

        if (schedule.getGroup() != null) {
            scheduleAdapter.checkGroupSchedule(command.getUser(), schedule);
        } else {
            scheduleAdapter.checkUserSchedule(command.getUser(), schedule);
        }

        schedule.deleteFile();
        fileStorageService.deleteFile(file);
    }
}
