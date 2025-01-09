package com.megatronix.eden.controller;

import java.io.Serializable;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.megatronix.eden.pojo.Tag;
import com.megatronix.eden.service.ITagService;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tag")
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "Endpoints for managing tags in the system.")
public class TagController {
  @Resource
  private ITagService tagService;

  /**
   * Creates a new tag in the system.
   *
   * @param tag The tag object to be created.
   * @return ResultResponse containing the outcome of the operation.
   */
  @PostMapping
  // @Operation(summary = "Create a new tag", description = "This endpoint allows
  // you to create a new tag in the system.", requestBody =
  // @RequestBody(description = "The tag details to be created.", required =
  // true))
  public ResultResponse<String> create(
      @Parameter(description = "The tag object to be created.", required = true) @RequestBody Tag tag) {
    log.error(tag.toString());
    return tagService.create(tag);
  }

}
