package com.example.gongdal.config.exception.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult{

  private T data;

}

