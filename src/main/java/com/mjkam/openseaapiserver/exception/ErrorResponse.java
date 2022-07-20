package com.mjkam.openseaapiserver.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(AppError appError) {
        this.code = appError.getCode();
        this.message = appError.getMessage();
    }
}
