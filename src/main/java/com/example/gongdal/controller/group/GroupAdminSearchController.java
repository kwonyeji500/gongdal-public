package com.example.gongdal.controller.group;

import com.example.gongdal.config.exception.response.ListResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.group.response.GroupAdminInfoResponse;
import com.example.gongdal.dto.group.command.GroupAdminInfoCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.group.GroupAdminSearchService;
import com.example.gongdal.service.group.GroupAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.gongdal.config.exception.code.ErrorResponseCode.NOT_AUTH_GROUP;
import static com.example.gongdal.config.exception.code.ErrorResponseCode.NOT_FOUND_GROUP;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/group/leader")
@Slf4j
@Tag(name = "2.3 그룹 관리자", description = "그룹 관리자 기능")
public class GroupAdminSearchController {
    private final ResponseService responseService;
    private final GroupAdminSearchService groupAdminSearchService;

    @GetMapping("/info/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_AUTH_GROUP})
    @Operation(summary = "관리자 그룹 정보 조회", description = "그룹 정보 수정을 위한 정보 조회 \n\n 방장 부방장만 가능합니다.")
    public SingleResult<GroupAdminInfoResponse> getAdminInfo(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                                             @PathVariable("groupId") long groupId) {
        log.info("[GroupAdminController] getAdminInfo - userId : {}, groupId : {}", user.getId(), groupId);
        return responseService.getSingleResult(GroupAdminInfoResponse.toRes(
                groupAdminSearchService.getAdminInfo(GroupAdminInfoCommand.toCommand(user, groupId))));
    }
}
