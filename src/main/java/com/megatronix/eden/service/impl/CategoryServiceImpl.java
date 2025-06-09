package com.megatronix.eden.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.megatronix.eden.enums.ResultEnum;
import com.megatronix.eden.pojo.Category;
import com.megatronix.eden.repository.CategoryRepository;
import com.megatronix.eden.service.ICategoryService;
import com.megatronix.eden.util.ResultResponse;

import jakarta.annotation.Resource;

@Service
public class CategoryServiceImpl implements ICategoryService {

  @Resource
  private CategoryRepository categoryRepository;

  @Override
  public ResultResponse<String> create(Category category) {
    categoryRepository.save(category);
    return ResultResponse.success(ResultEnum.SUCCESS, "create successfullu.");
  }

  @Override
  public ResultResponse<List<Category>> get() {
    return ResultResponse.success(ResultEnum.SUCCESS, categoryRepository.findAll());
  }

}
