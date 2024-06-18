package com.example.gongdal.controller.comment;

import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.comment.response.CommentGetResponse;
import com.example.gongdal.dto.comment.CommentGetChildrenCommand;
import com.example.gongdal.dto.comment.CommentGetCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.comment.CommentSearchService;
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

import java.util.List;

import static com.example.gongdal.config.exception.code.ErrorResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comment")
@Slf4j
@Tag(name = "4.2 댓글조회", description = "댓글 조회 관련 API")
public class CommentSearchController {
    private final ResponseService responseService;
    private final CommentSearchService commentSearchService;

    @GetMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_SCHEDULE, NOT_WRITE_PERSONAL})
    @Operation(summary = "게시글 댓글 조회")
    public SingleResult<Page<CommentGetResponse>> getComments(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                                              @PathVariable("scheduleId") long scheduleId,
                                                              @PageableDefault(page = 0, size = 10)
                                                                  @Parameter(hidden = true) Pageable pageable) {
        log.info("[CommentSearchController] getComments - userId: {}, scheduleId: {}", user.getId(), scheduleId);

        return responseService.getSingleResult(commentSearchService.getComments(
                CommentGetCommand.toCommand(user, scheduleId, pageable)));
    }

    @GetMapping("/{scheduleId}/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "대댓글 조회")
    public SingleResult<Page<CommentGetResponse>> getChildren(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                                              @PathVariable("scheduleId") long scheduleId,
                                                              @PathVariable("commentId") long commentId,
                                                              @PageableDefault(page = 0, size = 10)
                                                                  @Parameter(hidden = true) Pageable pageable) {
        log.info("[CommentSearchController] getChildren - userId: {}, scheduleId: {}, commentId: {}",
                user.getId(), scheduleId, commentId);

        return responseService.getSingleResult(commentSearchService.getChildren(
                CommentGetChildrenCommand.toCommand(user, scheduleId, commentId, pageable)));
    }
}
