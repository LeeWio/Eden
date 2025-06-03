package com.megatronix.eden.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "User account status enumeration")
public enum UserStatusEnum {

  @Schema(description = "The user account is active and can be used normally.")
  ACTIVE,

  @Schema(description = "The user account is inactive and cannot be used.")
  INACTIVE,

  @Schema(description = "The user account is pending and waiting for administrator approval.")
  PENDING,

  @Schema(description = "The user account is banned due to vialations or other reasons.")
  BANNED,

  @Schema(description = "The user account is deleted and can no longer be used.")
  DELETED

}
