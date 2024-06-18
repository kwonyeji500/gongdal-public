package com.example.gongdal.controller.schedule;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.schedule.request.CreateScheduleRequest;
import com.example.gongdal.controller.schedule.request.UpdateScheduleRequest;
import com.example.gongdal.dto.schedule.command.CreateScheduleCommand;
import com.example.gongdal.dto.schedule.command.DeleteScheduleCommand;
import com.example.gongdal.dto.schedule.command.DeleteScheduleFileCommand;
import com.example.gongdal.dto.schedule.command.UpdateScheduleCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.schedule.ScheduleManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.gongdal.config.exception.code.ErrorResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/schedule")
@Slf4j
@Tag(name = "3.1 일정관리", description = "일정 관리 관련 API")
public class ScheduleManageController {
    private final ResponseService responseService;
    private final ScheduleManageService scheduleManageService;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_AUTH_GROUP})
    @Operation(summary = "일정 생성")
    public CommonResult createSchedule(@RequestPart CreateScheduleRequest request,
                                           @RequestPart(required = false) MultipartFile multipartFile,
                                           @AuthenticationPrincipal @Parameter(hidden = true)User user) {
        log.info("[ScheduleManageController] createUserSchedule - request : {}, userId : {}", request.toString(), user.getId());

        scheduleManageService.createSchedule(CreateScheduleCommand.toCommand(request, user, multipartFile));

        return responseService.getSuccessResult();
    }

    @PostMapping(value = "/{scheduleId}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_SCHEDULE, NOT_AUTH_SCHEDULE})
    @Operation(summary = "일정 수정", description = "본인의 일정만 수정이 가능합니다. \n\n그룹일정은 방장,부방장,본인 만 수정이 가능합니다.")
    public CommonResult updateSchedule(@RequestPart UpdateScheduleRequest request,
                                       @RequestPart(required = false) MultipartFile multipartFile,
                                       @AuthenticationPrincipal @Parameter(hidden = true) User user,
                                       @PathVariable("scheduleId") long scheduleId) {
        log.info("[ScheduleManageController] updateSchedule - request : {}, userId : {}", request.toString(), user.getId());

        scheduleManageService.updateSchedule(UpdateScheduleCommand.toCommand(request,user, scheduleId, multipartFile));

        return responseService.getSuccessResult();
    }

    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_SCHEDULE, NOT_AUTH_SCHEDULE, NOT_AUTH_SCHEDULE})
    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.\n\n그룹일정은 방장, 부방장, 본인 만 삭제가 가능합니다.")
    public CommonResult deleteSchedule(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                       @PathVariable("scheduleId") long scheduleId) {
        log.info("[ScheduleManageController] deleteSchedule - scheduleId : {}, userId : {}", scheduleId, user.getId());

        scheduleManageService.deleteSchedule(DeleteScheduleCommand.toCommand(user, scheduleId));

        return responseService.getSuccessResult();
    }

    @DeleteMapping("/info/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_SCHEDULE, NOT_AUTH_SCHEDULE})
    @Operation(summary = "일정 사진 삭제")
    public CommonResult deleteFile(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                   @PathVariable("scheduleId") long scheduleId) {
        log.info("[ScheduleManageController] deleteFile - userId : {}, scheduleId : {}", user.getId(), scheduleId);

        scheduleManageService.deleteFile(DeleteScheduleFileCommand.toCommand(user, scheduleId));

        return responseService.getSuccessResult();
    }

}
