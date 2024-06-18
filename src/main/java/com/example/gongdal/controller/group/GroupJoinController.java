package com.example.gongdal.controller.group;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.group.request.GroupCreateRequest;
import com.example.gongdal.controller.group.request.GroupJoinRequest;
import com.example.gongdal.controller.group.response.GroupCreateResponse;
import com.example.gongdal.dto.group.command.GroupCreateCommand;
import com.example.gongdal.dto.group.command.GroupExitCommand;
import com.example.gongdal.dto.group.command.GroupJoinCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.group.GroupJoinService;
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
@RequestMapping("/v1")
@Slf4j
@Tag(name = "2.1 그룹 가입", description = "그룹 가입 & 생성 관련 API")
public class GroupJoinController {
    private final ResponseService responseService;
    private final GroupJoinService groupJoinService;

    @PostMapping(value = "/group/create", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_USER, GROUP_CREATE_RANGE_OVER})
    @Operation(summary = "그룹 생성", description = "그룹을 생성합니다. \n\n 생성한 유저는 방장이 됩니다.")
    public SingleResult<GroupCreateResponse> createGroup(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                                         @RequestPart GroupCreateRequest request,
                                                         @RequestPart(required = false) MultipartFile multipartFile) {

        return responseService.getSingleResult(GroupCreateResponse.toRes(
                groupJoinService.createGroup(GroupCreateCommand.toCommand(user, request, multipartFile))));
    }

    @PostMapping("/group/join")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {GROUP_LOGIN_ERROR, NOT_FOUND_GROUP, NOT_FOUND_USER, GROUP_JOIN_RANGE_OVER, GROUP_COUNT_RANGE_OVER})
    @Operation(summary = "그룹 가입", description = "그룹에 가입합니다. \n\n 일반회원으로 가입됩니다.")
    public CommonResult joinGroup(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                  @RequestBody GroupJoinRequest request) {

        groupJoinService.joinGroup(GroupJoinCommand.toCommand(user, request));
        return responseService.getSuccessResult();
    }

    @PostMapping("/group/exit/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_FOUND_USER, NOT_FOUND_GROUP_USER, NOT_EXIT_LEADER})
    @Operation(summary = "그룹 탈퇴", description = "그룹에서 탈퇴합니다. \n\n 방장은 탈퇴할 수 없습니다.")
    public CommonResult exitGroup(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                  @PathVariable("groupId") long groupId) {

        groupJoinService.exitGroup(GroupExitCommand.toCommand(user, groupId));
        return responseService.getSuccessResult();
    }
}
