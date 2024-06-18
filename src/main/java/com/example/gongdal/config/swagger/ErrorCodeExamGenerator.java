package com.example.gongdal.config.swagger;


import com.example.gongdal.config.exception.code.ErrorResponseCode;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ErrorCodeExamGenerator {

  ErrorResponseCode[] value() default ErrorResponseCode.UNKNOWN_ERROR;

}
