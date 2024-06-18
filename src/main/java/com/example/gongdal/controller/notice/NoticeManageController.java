package com.example.gongdal.controller.notice;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.notice.response.NoticeGetResponse;
import com.example.gongdal.dto.notice.NoticeGetCommand;
import com.example.gongdal.dto.notice.NoticeUpdateCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.notice.NoticeManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notice")
@Slf4j
@Tag(name = "5.1 알림관리", description = "알림관리기능")
public class NoticeManageController {
    private final ResponseService responseService;
    private final NoticeManageService noticeManageService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "해당 유저의 알림을 불러옵니다.")
    public SingleResult<Page<NoticeGetResponse>> getNotice(@AuthenticationPrincipal @Parameter(hidden = true)User user,
                                                           @PageableDefault(page = 0, size = 10)
                                                           @Parameter(hidden = true) Pageable pageable) {
        log.info("[NoticeController] getNotice - userId: {}", user.getId());

        return responseService.getSingleResult(noticeManageService.getNotice(NoticeGetCommand.toCommand(user, pageable)));
    }

    @PostMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "해당 유저의 알림을 읽음 처리 합니다.", description = "종모양 클릭 시 안읽엇던 알림 모두 읽음처리 됨")
    public CommonResult updateStatus(@AuthenticationPrincipal @Parameter(hidden = true)User user) {
        log.info("[NoticeController] updateStatus - userId: {}",user.getId());

        noticeManageService.updateNotice(NoticeUpdateCommand.toCommand(user));
        return responseService.getSuccessResult();
    }
}
