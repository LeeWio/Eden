package com.megatronix.eden.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Article status enumeration")
public enum ArticleStatusEnum {

  @Schema(description = "The article is under review and awaiting approval.")
  REVIEW,

  @Schema(description = "The article is in draft state and not yet ready for publication.")
  DRAFT,

  @Schema(description = "The article has been published and is publicly accessible.")
  PUBLISHED,

  @Schema(description = "The article has been archived and is no longer publicly accessible.")
  ARCHIVED;

}
