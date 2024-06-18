package com.example.gongdal.controller.user;

import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.user.request.UserUpdateRequest;
import com.example.gongdal.controller.user.response.UserGroupInfoResponse;
import com.example.gongdal.controller.user.response.UserInfoResponse;
import com.example.gongdal.dto.user.command.UserDeleteProfileCommand;
import com.example.gongdal.dto.user.command.UserGroupInfoCommand;
import com.example.gongdal.dto.user.command.UserInfoCommand;
import com.example.gongdal.dto.user.command.UserUpdateCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.user.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Slf4j
@Tag(name = "1.2 회원 정보", description = "회원 정보 관련 API")
public class UserInfoController {
    private final ResponseService responseService;
    private final UserInfoService userInfoService;

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "회원정보조회")
    public SingleResult<UserInfoResponse> getUser(@AuthenticationPrincipal @Parameter(hidden = true) User user) {
        return responseService.getSingleResult(userInfoService.getUserInfo(UserInfoCommand.toCommand(user)));
    }

    @PostMapping(value = "/info", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원정보수정")
    public CommonResult updateUser(@AuthenticationPrincipal @Parameter(hidden = true) User user,
                                                     @RequestPart(required = false) MultipartFile multipartFile,
                                                     @RequestPart(required = false) UserUpdateRequest request) {
        userInfoService.updateUser(UserUpdateCommand.toCommand(user, request, multipartFile));

        return responseService.getSuccessResult();
    }

    @GetMapping("/info/group")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "마이 그룹 조회")
    public SingleResult<List<UserGroupInfoResponse>> getUserGroup(@AuthenticationPrincipal @Parameter(hidden = true) User user) {
        log.info("[UserInfoController] getUserGroup - userId : {}", user.getId());

        return responseService.getSingleResult(
                userInfoService.getUserGroup(UserGroupInfoCommand.toCommand(user)).stream().map(UserGroupInfoResponse::toRes).toList());
    }

    @DeleteMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "프로필사진 삭제")
    public CommonResult deleteProfile(@AuthenticationPrincipal @Parameter(hidden = true) User user) {
        log.info("[UserInfoController] deleteProfile - userId: {}", user.getId());
        userInfoService.deleteProfile(UserDeleteProfileCommand.toCommand(user));

        return responseService.getSuccessResult();
    }
}
