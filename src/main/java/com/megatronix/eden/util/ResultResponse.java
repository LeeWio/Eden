package com.megatronix.eden.util;

import lombok.Data;
import com.megatronix.eden.enums.*;

@Data
public class ResultResponse<T> {
  private Integer status;
  private String message;
  private T data;

  public ResultResponse(ResultEnum resultEnum) {
    this.status = resultEnum.getStatus();
    this.message = resultEnum.getMessage();
  }

  public ResultResponse(ResultEnum resultEnum, T data) {
    this(resultEnum);
    this.data = data;
  }

  public static <T> ResultResponse<T> success(ResultEnum resultEnum, T data) {
    return new ResultResponse<>(resultEnum, data);
  }

  public static <T> ResultResponse<T> error(ResultEnum resultEnum) {
    return new ResultResponse<>(resultEnum);
  }

}
