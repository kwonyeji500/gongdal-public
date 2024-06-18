package com.example.gongdal.controller.comment;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.comment.request.CommentCreateRequest;
import com.example.gongdal.controller.comment.request.CommentUpdateRequest;
import com.example.gongdal.controller.comment.response.CommentGetResponse;
import com.example.gongdal.dto.comment.CommentCreateCommand;
import com.example.gongdal.dto.comment.CommentDeleteCommand;
import com.example.gongdal.dto.comment.CommentUpdateCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.comment.CommentManageService;
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
@RequestMapping("/v1/comment")
@Slf4j
@Tag(name = "4.1 댓글관리", description = "댓글 관리 관련 API")
public class CommentManageController {
    private final ResponseService responseService;
    private final CommentManageService commentManageService;

    @PostMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_SCHEDULE,NOT_FOUND_GROUP_USER, NOT_FOUND_COMMENT})
    @Operation(summary = "댓글 생성")
    public SingleResult<CommentGetResponse> createComment(@AuthenticationPrincipal @Parameter(hidden = true)User user,
                                                          @RequestBody CommentCreateRequest request,
                                                          @PathVariable("scheduleId") long scheduleId) {
        log.info("[CommentManageController] createComment - userId : {}, scheduleId : {}, request : {}",
                user.getId(), scheduleId, request.toString());

        return responseService.getSingleResult(CommentGetResponse.toCreateRes(
                commentManageService.createComment(CommentCreateCommand.toCommand(user, scheduleId, request))));
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_COMMENT})
    @Operation(summary = "댓글 수정", description = "본인의 댓글만 수정이 가능합니다.")
    public CommonResult retouchComment(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                       @RequestBody CommentUpdateRequest request,
                                       @PathVariable("commentId") long commentId) {
        log.info("[CommentManageController] retouchComment - userId : {}, commentId : {}, request : {}",
                user.getId(), commentId, request.toString());
        commentManageService.updateComment(CommentUpdateCommand.toCommand(user, request, commentId));

        return responseService.getSuccessResult();
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_COMMENT})
    @Operation(summary = "댓글 삭제", description = "방장, 부방장, 본인만 댓글 삭제가 가능합니다.")
    public CommonResult deleteComment(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                      @PathVariable("commentId") long commentId) {
        log.info("CommentManageController] deleteComment - userId : {}, commentId : {}", user.getId(), commentId);
        commentManageService.deleteComment(CommentDeleteCommand.toCommand(user, commentId));

        return responseService.getSuccessResult();
    }

}
