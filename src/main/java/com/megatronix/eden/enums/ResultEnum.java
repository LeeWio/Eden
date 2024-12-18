package com.megatronix.eden.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

  SUCCESS(200, "SUCCESS"),
  INVALID_EMAIL_FORMAT(4001, "Invalid email format"),
  USER_NOT_FOUND(4002, "user not found."),
  USER_EXIST(4003, "user alealy exist.");

  private final Integer status;
  private final String message;

  ResultEnum(Integer status, String message) {
    this.status = status;
    this.message = message;
  }
}
