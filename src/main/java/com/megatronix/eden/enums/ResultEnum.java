package com.megatronix.eden.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

  SUCCESS(200, "SUCCESS");

  private final Integer status;
  private final String message;

  ResultEnum(Integer status, String message) {
    this.status = status;
    this.message = message;
  }
}
