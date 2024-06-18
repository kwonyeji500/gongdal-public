package com.example.gongdal.config.exception.response;

import com.example.gongdal.config.exception.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

  public <T> SingleResult<T> getSingleResult(T data) {
    SingleResult<T> result = new SingleResult<>();
    result.setData(data);
    setSuccessResult(result);
    return result;
  }

  public <T> ListResult<T> getListResult(List<T> list) {
    ListResult<T> result = new ListResult<>();
    result.setList(list);
    setSuccessResult(result);
    return result;

  }

  public CommonResult getSuccessResult() {
    CommonResult result = new CommonResult();
    setSuccessResult(result);
    return result;
  }

  public ResponseEntity<CommonResult> getFailResult(ResponseCode err) {
    CommonResult result = new CommonResult();
    result.setSuccess(false);
    result.setCode(err.getCode());
    result.setMsg(err.getMsg());
    return ResponseEntity.status(err.getStatus()).body(result);
  }

  public <T> ResponseEntity<ListResult<T>> getFailResults(ResponseCode err, List errors) {
    ListResult<T> result = new ListResult<>();
    result.setSuccess(false);
    result.setCode(err.getCode());
    result.setMsg(err.getMsg());
    result.setList(errors);
    return ResponseEntity.status(err.getStatus()).body(result);
  }

  private void setSuccessResult(CommonResult result) {
    result.setSuccess(true);
    result.setCode(CommonResponse.SUCCESS.getCode());
    result.setMsg(CommonResponse.SUCCESS.getMsg());
  }

  /**
   * 코드와 Msg값을 가지는 열거형이다.
   */
  @Getter
  @AllArgsConstructor
  public enum CommonResponse {
    SUCCESS("0", "성공하였습니다."),
    FAIL("-1", "실패하였습니다.");
    private String code;
    private String msg;
  }

}

