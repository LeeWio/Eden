package com.megatronix.eden.service;

import org.springframework.stereotype.Service;

import com.megatronix.eden.pojo.Category;
import com.megatronix.eden.util.ResultResponse;

@Service
public interface ICategoryService {
  ResultResponse<String> create(Category category);
}
