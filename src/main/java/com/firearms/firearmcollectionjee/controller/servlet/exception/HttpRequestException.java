package com.firearms.firearmcollectionjee.controller.servlet.exception;

import lombok.Getter;

public class HttpRequestException extends RuntimeException {
    @Getter
    private final int responseCode;

    public HttpRequestException(int responseCode) {
        this.responseCode = responseCode;
    }

    public HttpRequestException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public HttpRequestException(String message, Throwable cause, int responseCode) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public HttpRequestException(Throwable cause, int responseCode) {
        super(cause);
        this.responseCode = responseCode;
    }

    public HttpRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int responseCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseCode = responseCode;
    }
}
