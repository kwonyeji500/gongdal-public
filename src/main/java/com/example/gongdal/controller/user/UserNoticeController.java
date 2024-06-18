package com.example.gongdal.controller.user;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.user.request.UserNoticeUpdateRequest;
import com.example.gongdal.dto.user.command.UserNoticeUpdateCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.user.UserNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.gongdal.config.exception.code.ErrorResponseCode.NOT_FOUND_USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notice")
@Slf4j
@Tag(name = "1.3 회원 알림", description = "회원 알림 설정 API")
public class UserNoticeController {
    private final ResponseService responseService;
    private final UserNoticeService userNoticeService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_USER})
    @Operation(summary = "회원 알림 설정")
    public CommonResult updateNotice(@AuthenticationPrincipal @Parameter(hidden = true)User user,
                                     @RequestBody UserNoticeUpdateRequest request) {
        log.info("[UserNoticeController] updateNotice - userId: {}, request: {}", user.getId(), request.toString());
        userNoticeService.updateNotice(UserNoticeUpdateCommand.toCommand(user,request));

        return responseService.getSuccessResult();
    }
}
