package com.example.gongdal.controller.group;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.group.request.GroupCommissionRequest;
import com.example.gongdal.dto.group.command.GroupCommissionCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.group.GroupAdminRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import static com.example.gongdal.config.exception.code.ErrorResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/group/leader")
@Slf4j
@Tag(name = "2.3 그룹 관리자", description = "그룹 관리자 기능")
public class GroupAdminRoleController {
    public final ResponseService responseService;
    public final GroupAdminRoleService groupAdminRoleService;

    @PostMapping(value = "/commission")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_FOUND_GROUP_USER, NOT_AUTH_GROUP, NOT_VALID_ROLE})
    @Operation(summary = "방장 위임")
    public CommonResult commissionLeader(@AuthenticationPrincipal @Parameter(hidden = true)User user,
                                         @RequestBody GroupCommissionRequest request) {
        log.info("[GroupAdminRoleController] commissionLeader - userId : {}, request : {}", user.getId(), request.toString());

        groupAdminRoleService.commissionLeader(GroupCommissionCommand.toCommand(user, request));
        return responseService.getSuccessResult();
    }

    @PostMapping(value = "/commission/subLeader")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_FOUND_GROUP_USER})
    @Operation(summary = "부방장 위임/해임", description = "기존 부방장일 경우 해임, 일반회원일경우 위임")
    public CommonResult commissionSubLeader(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                            @RequestBody GroupCommissionRequest request) {
        log.info("[GroupAdminRoleController] commissionSubLeader - userId : {}, request : {}", user.getId(), request.toString());

        groupAdminRoleService.commissionSubLeader(GroupCommissionCommand.toCommand(user, request));
        return responseService.getSuccessResult();
    }
}
