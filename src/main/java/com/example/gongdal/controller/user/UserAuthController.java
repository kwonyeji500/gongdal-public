package com.example.gongdal.controller.user;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.user.request.LoginUserRequest;
import com.example.gongdal.controller.user.request.NormalUserJoinRequest;
import com.example.gongdal.controller.user.request.SocialCheckRequest;
import com.example.gongdal.controller.user.request.TokenRenewRequest;
import com.example.gongdal.controller.user.response.LoginUserResponse;
import com.example.gongdal.controller.user.response.TokenRenewResponse;
import com.example.gongdal.dto.token.TokenRenewCommand;
import com.example.gongdal.dto.user.command.NormalUserJoinCommand;
import com.example.gongdal.dto.user.command.NormalUserLoginCommand;
import com.example.gongdal.dto.user.command.SocialCheckCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.service.user.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.gongdal.config.exception.code.ErrorResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Slf4j
@Tag(name = "1.1 회원 인증", description = "회원 인증 관련 API")
public class UserAuthController {
    private final ResponseService responseService;
    private final UserAuthService userAuthService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_USER,USER_LOGIN_ERROR})
    @Operation(summary = "일반회원 로그인", description = "일반 회원 로그인 기능 (소셜X)")
    public SingleResult<LoginUserResponse> login(@RequestBody @Validated LoginUserRequest request) {
        log.info("[UserAuthController] login - Request : {}", request.toString());
        return responseService.getSingleResult(
                LoginUserResponse.toRes(userAuthService.loginUser(NormalUserLoginCommand.toCommand(request))));
    }

    @PostMapping("/renew")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {USER_TOKEN_VALIDA,NOT_FOUND_USER})
    @Operation(summary = "엑세스 토큰 재발급", description = "리프레쉬 토큰으로 엑세스 토큰을 재발급합니다. \n\n " +
            "엑세스 토큰이 만료되지 않았거나 리프레쉬 토큰이 만료되었을 경우 재발급이 불가합니다.")
    public SingleResult<TokenRenewResponse> renew(HttpServletRequest request,
                                                  @RequestBody TokenRenewRequest renewRequest) {
        return responseService.getSingleResult(TokenRenewResponse.toRes(userAuthService.renewToken(
                TokenRenewCommand.toCommand(request,renewRequest))));
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "로그아웃", description = "회원 로그아웃 기능")
    public CommonResult logout(@AuthenticationPrincipal @Parameter(hidden = true) User user) {

        userAuthService.logoutUser(user);

        return responseService.getSuccessResult();
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {
            ErrorResponseCode.DUPLICATE_USERID
    })
    @Operation(summary = "일반회원가입", description = "일반유저 회원가입")
    public CommonResult joinUser(
            @RequestBody @Valid NormalUserJoinRequest request) {
        log.info("[UserJoinController] joinUser - Request = {}", request.toString());

        userAuthService.joinNormal(NormalUserJoinCommand.toCommand(request));

        return responseService.getSuccessResult();
    }

    @PostMapping("/social")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "소셜 회원가입 & 로그인", description = "가입이력이 있을 시 로그인 else 회원가입")
    public SingleResult<LoginUserResponse> socialUser(
            @RequestBody @Valid SocialCheckRequest request) {
        log.info("[UserJoinController] socialUser - Request = {}", request.toString());

        return responseService.getSingleResult(
                LoginUserResponse.toRes(userAuthService.checkOauth(SocialCheckCommand.toCommand(request))));
    }

}
