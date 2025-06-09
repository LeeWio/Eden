package com.megatronix.eden.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.megatronix.eden.pojo.Article;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Service
@Tag(name = "Article Management", description = "Operations related to Article management")
public interface IArticleService {

  /**
   * Creates a new article.
   *
   * @param article The article object to be created.
   * @return A ResultResponse containing the result of the article creation
   *         operation.
   */
  @Operation(summary = "Create a new article", description = "This operation allows you to create a new article in the system.")
  ResultResponse<String> create(
      @Parameter(description = "The article object to be created.", required = true) Article article);

  /**
   * Retrieves a list of all articles.
   *
   * @return A ResultResponse containing a list of all available articles.
   */
  @Operation(summary = "Get all articles", description = "This operation retrieves a list of all articles currently stored in the system.")
  ResultResponse<List<Article>> get();
}
