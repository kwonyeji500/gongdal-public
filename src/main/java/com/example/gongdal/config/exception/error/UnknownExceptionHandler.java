package com.example.gongdal.config.exception.error;


import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Order(100)
@RequiredArgsConstructor
@Slf4j
public class UnknownExceptionHandler {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<CommonResult> handleException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return responseService.getFailResult(ErrorResponseCode.UNKNOWN_ERROR);
    }

}