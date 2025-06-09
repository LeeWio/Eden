package com.megatronix.eden.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.megatronix.eden.pojo.Category;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Interface for managing category-related operations in the system.
 */
@Service
@Tag(name = "Category Management", description = "Endpoints for creating and retrieving categories")
public interface ICategoryService {

  /**
   * Creates a new category in the system.
   *
   * @param category The category object to be created.
   * @return A ResultResponse containing the result of the operation.
   */
  @Operation(summary = "Create a new category", description = "Creates a new category and persists it in the database.")
  ResultResponse<String> create(
      @Parameter(description = "The category object to be created.", required = true) Category category);

  /**
   * Retrieves all categories from the system.
   *
   * @return A ResultResponse containing a list of all available categories.
   */
  @Operation(summary = "Get all categories", description = "Fetches a list of all categories currently stored in the system.")
  ResultResponse<List<Category>> get();
}
