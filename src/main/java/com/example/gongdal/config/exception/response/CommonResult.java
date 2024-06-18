package com.example.gongdal.config.exception.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

  private boolean success;
  private String code;
  private String msg;

}