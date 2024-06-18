package com.example.gongdal.config.exception.error;


import com.example.gongdal.config.exception.response.CommonResult;
import com.example.gongdal.config.exception.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
  private final ResponseService responseService;

  /*
   * Developer Custom Exception
   */
  @ExceptionHandler(CustomRuntimeException.class)
  protected ResponseEntity<CommonResult> handleCustomException(final CustomRuntimeException e) {
    return responseService.getFailResult(e.getResponseCode());
  }
}
