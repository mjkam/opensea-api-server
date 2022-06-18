package com.mjkam.openseaapiserver.util;

public class VerifySignatureException extends RuntimeException{
    public VerifySignatureException(String message) {
        super(message);
    }

    public VerifySignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
