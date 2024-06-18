package com.example.gongdal.controller.group;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.dto.group.command.GroupDeleteCommand;
import com.example.gongdal.dto.group.command.GroupDeleteCoverCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.group.GroupAdminDeleteService;
import com.example.gongdal.service.group.GroupAdminService;
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
public class GroupAdminDeleteController {
    private final ResponseService responseService;
    private final GroupAdminDeleteService groupAdminDeleteService;

    @DeleteMapping("/info/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_FOUND_FILE, NOT_AUTH_GROUP})
    @Operation(summary = "그룹 커버사진 삭제")
    public CommonResult deleteCover(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                    @PathVariable("groupId") long groupId) {
        log.info("[GroupAdminController] deleteCover - userId: {}, groupId : {}", user.getId(), groupId);
        groupAdminDeleteService.deleteCover(GroupDeleteCoverCommand.toCommand(user, groupId));

        return responseService.getSuccessResult();
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_GROUP, NOT_AUTH_GROUP})
    @Operation(summary = "그룹 삭제", description = "방장만 그룹을 삭제할 수 있습니다.")
    public CommonResult deleteGroup(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                    @PathVariable("groupId") long groupId) {
        log.info("[GroupAdminController] deleteGroup - groupId : {}, userId : {}", groupId, user.getId());
        groupAdminDeleteService.deleteGroup(GroupDeleteCommand.toCommand(user, groupId));

        return responseService.getSuccessResult();
    }
}
