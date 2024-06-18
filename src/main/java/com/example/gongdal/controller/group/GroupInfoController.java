package com.example.gongdal.controller.group;


import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.group.request.GroupMemberListRequest;
import com.example.gongdal.controller.group.request.GroupSearchInfoRequest;
import com.example.gongdal.controller.group.response.GroupGetFeedResponse;
import com.example.gongdal.controller.group.response.GroupKeyResponse;
import com.example.gongdal.controller.group.response.GroupMemberListResponse;
import com.example.gongdal.controller.group.response.GroupSearchInfoResponse;
import com.example.gongdal.dto.group.command.GroupGetFeedCommand;
import com.example.gongdal.dto.group.command.GroupKeyCommand;
import com.example.gongdal.dto.group.command.GroupMemberListCommand;
import com.example.gongdal.dto.group.command.GroupSearchInfoCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.group.GroupInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.gongdal.config.exception.code.ErrorResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/group")
@Slf4j
@Tag(name = "2.2 그룹 정보", description = "그룹 정보조회 API")
public class GroupInfoController {

    private final ResponseService responseService;
    private final GroupInfoService groupInfoService;

    @GetMapping("/key/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP_USER, NOT_FOUND_GROUP})
    @Operation(summary = "그룹 키 조회", description = "그룹의 키를 조회합니다. \n\n 그룹에 속해있는 유저만 조회 가능합니다.")
    public SingleResult<GroupKeyResponse> getKey(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                                 @PathVariable("groupId") Long groupId) {
        log.info("[GroupInfoController] getKey - userId : {}, groupId : {}", user.getId(), groupId);

        return responseService.getSingleResult(GroupKeyResponse.toRes(
                groupInfoService.getKey(GroupKeyCommand.toCommand(user, groupId))));
    }

    @PostMapping("/info/search")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP})
    @Operation(summary = "그룹 정보 조회", description = "그룹 검색 시 사용될 그룸의 정보를 조회합니다.")
    public SingleResult<GroupSearchInfoResponse> getInfo(@RequestBody GroupSearchInfoRequest request) {
        log.info("[GroupInfoController] getInfo - request : {}", request.toString());

        return responseService.getSingleResult(GroupSearchInfoResponse.toRes(
                groupInfoService.getInfo(GroupSearchInfoCommand.toCommand(request))));
    }

    @PostMapping("/{groupId}/member/list")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_AUTH_GROUP})
    @Operation(summary = "그룹 참여자 명단 조회", description = "닉네임으로 검색이 가능합니다.")
    public SingleResult<GroupMemberListResponse> getMemberList(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                                               @PathVariable("groupId") long groupId,
                                                               @RequestBody GroupMemberListRequest request,
                                                               @PageableDefault(page = 0, size = 10)
                                                               @Parameter(hidden = true) Pageable pageable) {
        log.info("[GroupInfoController] getMemberList - userId : {}, grouId : {}, request : {}",
                user.getId(), groupId, request.toString());

        return responseService.getSingleResult(GroupMemberListResponse.toRes(
                groupInfoService.getMemberList(GroupMemberListCommand.toCommand(user, groupId, request, pageable))));
    }

    @GetMapping("/feed/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP_USER})
    @Operation(summary = "그룹 피드 조회")
    public SingleResult<Page<GroupGetFeedResponse>> getGroupFeed(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                                                 @PathVariable("groupId") long groupId,
                                                                 @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                                 @Parameter(hidden = true) Pageable pageable) {
        log.info("[GroupInfoController] getGroupFeed - userId: {}, groupId: {}", user.getId(), groupId);

        return responseService.getSingleResult(
                groupInfoService.getGroupFeed(GroupGetFeedCommand.toCommand(user, groupId, pageable)).map(GroupGetFeedResponse::toRes));
    }

}
