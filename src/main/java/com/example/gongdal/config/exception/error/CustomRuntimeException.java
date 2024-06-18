package com.example.gongdal.config.exception.error;


import com.example.gongdal.config.exception.code.ResponseCode;

public class CustomRuntimeException extends RuntimeException {

  private final ResponseCode responseCode;

  public CustomRuntimeException(String message, ResponseCode responseCode) {
    super(message);
    this.responseCode = responseCode;
  }

  public CustomRuntimeException(ResponseCode responseCode) {
    super();
    this.responseCode = responseCode;
  }

  public ResponseCode getResponseCode() {
    return responseCode;
  }
}

