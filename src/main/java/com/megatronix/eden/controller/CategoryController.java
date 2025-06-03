package com.megatronix.eden.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.megatronix.eden.pojo.Category;
import com.megatronix.eden.service.ICategoryService;
import com.megatronix.eden.util.ResultResponse;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/category")
@io.swagger.v3.oas.annotations.tags.Tag(name = "CategoryController", description = "Endpoints for managing categorys in the system.")
public class CategoryController {

  @Resource
  private ICategoryService categoryService;

  @PostMapping
  public ResultResponse<String> create(@RequestBody Category category) {
    return categoryService.create(category);
  }
}
