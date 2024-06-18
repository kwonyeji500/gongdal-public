package com.example.gongdal.controller.schedule;

import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.schedule.request.GetScheduleUserRequest;
import com.example.gongdal.controller.schedule.response.GetScheduleDetailResponse;
import com.example.gongdal.controller.schedule.response.GetUserScheduleResponse;
import com.example.gongdal.dto.schedule.command.GetScheduleDetailCommand;
import com.example.gongdal.dto.schedule.command.GetScheduleUserCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.schedule.ScheduleSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.gongdal.config.exception.code.ErrorResponseCode.NOT_FOUND_SCHEDULE;
import static com.example.gongdal.config.exception.code.ErrorResponseCode.NOT_FOUND_USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/schedule")
@Slf4j
@Tag(name = "3.2 일정조회", description = "일정 조회 관련 API")
public class ScheduleSearchController {
    private final ResponseService responseService;
    private final ScheduleSearchService scheduleSearchService;

    @GetMapping("/detail/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_SCHEDULE})
    @Operation(summary = "일정 상세조회")
    public SingleResult<GetScheduleDetailResponse> getScheduleDetail(@PathVariable("scheduleId") long scheduleId,
                                                                     @AuthenticationPrincipal @Parameter(hidden = true) User user) {
        log.info("[ScheduleSearchController] getScheduleDetail - scheduleId: {}, userId: {}", scheduleId, user.getId());
        return responseService.getSingleResult(GetScheduleDetailResponse.toRes(
                scheduleSearchService.getDetail(GetScheduleDetailCommand.toCommand(scheduleId, user))));
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_USER})
    @Operation(summary = "유저 일정조회")
    public SingleResult<List<GetUserScheduleResponse>> getUserSchedule(
            @RequestBody GetScheduleUserRequest request,
            @AuthenticationPrincipal @Parameter(hidden = true) User user) {
        log.info("[ScheduleSearchController] getUserSchedule - userId: {}, request: {}", user.getId(), request.toString());

        return responseService.getSingleResult(
                scheduleSearchService.getUserSchedule(GetScheduleUserCommand.toCommand(user, request)).stream().map(GetUserScheduleResponse::toRes).toList()
        );
    }
}
