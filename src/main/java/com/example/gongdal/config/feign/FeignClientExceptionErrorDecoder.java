package com.example.gongdal.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignClientExceptionErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 400:
                return new RuntimeException("잘못된 요청입니다.");
            case 404:
                return new RuntimeException("페이지를 찾을 수 없습니다.");
            case 500:
            case 505:
            case 503:
                return new RuntimeException("서버오류");
            default:
                return new RuntimeException("알수없는 오류가 발생했습니다.");
        }
    }
}
