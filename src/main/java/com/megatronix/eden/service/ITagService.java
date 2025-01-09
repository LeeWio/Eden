package com.megatronix.eden.service;

import org.springframework.stereotype.Service;

import com.megatronix.eden.pojo.Tag;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Service
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Management", description = "Operations related to Tag management")
public interface ITagService {
  /**
   * Creates a new tag.
   *
   * @param tag The tag object to be created.
   * @return A ResultResponse containing the result of the tag creation operation.
   */
  @Operation(summary = "Create a new tag", description = "This operation allows you to create a new tag in the system.")
  ResultResponse<String> create(@Parameter(description = "The tag object to be created.", required = true) Tag tag);
}
