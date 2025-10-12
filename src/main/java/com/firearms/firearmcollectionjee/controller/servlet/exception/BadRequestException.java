package com.firearms.firearmcollectionjee.controller.servlet.exception;

public class BadRequestException extends HttpRequestException {
  private static final int RESPONSE_CODE = 400;

  public BadRequestException() {
    super(RESPONSE_CODE);
  }

  public BadRequestException(String message) {
    super(message, RESPONSE_CODE);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause, RESPONSE_CODE);
  }

  public BadRequestException(Throwable cause) {
    super(cause, RESPONSE_CODE);
  }

  public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace, RESPONSE_CODE);
  }
}
