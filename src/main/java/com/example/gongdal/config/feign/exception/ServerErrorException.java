package com.example.gongdal.config.feign.exception;

public class ServerErrorException extends RuntimeException{

    public ServerErrorException(String message) {
        super(message);
    }
}
