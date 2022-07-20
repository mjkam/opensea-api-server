package com.mjkam.openseaapiserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("AppError: {}, message: {}", AppError.INVALID_REQUEST.getMessage(), e.getMessage(), e);
        return responseEntity(AppError.INVALID_REQUEST);
    }

    private ResponseEntity<ErrorResponse> responseEntity(AppError appError) {
        return new ResponseEntity<>(
                new ErrorResponse(appError),
                appError.getHttpStatus()
        );
    }
}
