package com.example.gongdal.controller.group;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.group.request.GroupInfoChangeRequest;
import com.example.gongdal.controller.group.request.GroupKickRequest;
import com.example.gongdal.controller.group.response.GroupAdminInfoResponse;
import com.example.gongdal.dto.group.command.*;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.group.GroupAdminService;
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
@RequestMapping("/v1/group/leader")
@Slf4j
@Tag(name = "2.3 그룹 관리자", description = "그룹 관리자 기능")
public class GroupAdminController {
    private final ResponseService responseService;
    private final GroupAdminService groupAdminService;

    @PostMapping(value = "/info/{groupId}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_AUTH_GROUP})
    @Operation(summary = "그룹 정보 수정", description = "그룹 정보를 수정합니다. \n\n 방장과 부방장만 가능합니다.")
    public CommonResult changeInfo(@RequestPart(required = false) GroupInfoChangeRequest request,
                                   @RequestPart(required = false) MultipartFile multipartFile,
                                   @PathVariable("groupId") long groupId,
                                   @AuthenticationPrincipal @Parameter(hidden = true) User user) {
        log.info("[GroupAdminController] changeInfo - request : {}, groupId : {}, userId : {}", request.toString(), groupId, user.getId());
        groupAdminService.changeInfo(GroupInfoChangeCommand.toCommand(request, groupId, user, multipartFile));

        return responseService.getSuccessResult();
    }

    @PostMapping("/member/exit")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_VALID_KICK, NOT_KICK_LEADER, NOT_MATCH_KICK, NOT_FOUND_USER})
    @Operation(summary = "그룹 멤버 강퇴", description = "관리자가 그룹에서 해당 멤버를 강제탈퇴 시킵니다.")
    public CommonResult kickMember(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                   @RequestBody GroupKickRequest request) {
        log.info("[GroupAdminController] kickMember - userId : {}, request : {}", user.getId(), request);
        groupAdminService.kickMember(GroupKickCommand.toCommand(user, request));

        return responseService.getSuccessResult();
    }
}
