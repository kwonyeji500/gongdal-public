package com.example.gongdal.config.exception.code;

import org.springframework.http.HttpStatus;

public interface ResponseCode {

  HttpStatus getStatus();
  String getCode();
  String getMsg();

}

