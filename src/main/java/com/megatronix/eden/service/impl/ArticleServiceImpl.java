package com.megatronix.eden.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.megatronix.eden.enums.ResultEnum;
import com.megatronix.eden.pojo.Article;
import com.megatronix.eden.repository.ArticleRepository;
import com.megatronix.eden.service.IArticleService;
import com.megatronix.eden.util.ResultResponse;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleServiceImpl implements IArticleService {
  @Resource
  private ArticleRepository articleRepository;

  @Override
  public ResultResponse<String> create(Article article) {
    articleRepository.save(article);
    return ResultResponse.success(ResultEnum.SUCCESS, "create successfully");
  }

  @Override
  public ResultResponse<List<Article>> get() {
    return ResultResponse.success(ResultEnum.SUCCESS, articleRepository.findAll());
  }
}
