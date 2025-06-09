package com.megatronix.eden.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.megatronix.eden.pojo.Tag;
import com.megatronix.eden.service.ITagService;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tag")
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "TagController", description = "Endpoints for managing tags in the system.")
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
  @Operation(summary = "Create a new tag", description = "Creates a new tag and persists it in the database.")
  public ResultResponse<String> create(
      @Parameter(description = "The tag object to be created.", required = true) @RequestBody Tag tag) {
    return tagService.create(tag);
  }

  /**
   * Retrieves all tags from the system.
   *
   * @return ResultResponse containing a list of all available tags.
   */
  @GetMapping
  @Operation(summary = "Get all tags", description = "Fetches a list of all tags currently stored in the system.")
  public ResultResponse<List<Tag>> get() {
    return tagService.get();
  }

}
