package com.mjkam.openseaapiserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppError {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "client bad request"),
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;

    AppError(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
