package com.megatronix.eden.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.megatronix.eden.pojo.Article;
import com.megatronix.eden.service.IArticleService;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/article")
@Tag(name = "ArticleController")
@Slf4j
public class ArticleController {
  @Resource
  private IArticleService articleService;

  @GetMapping
  public ResultResponse<List<Article>> get() {
    return articleService.get();
  }

  @PostMapping
  public ResultResponse<String> create(@RequestBody Article article) {
    return articleService.create(article);
  }
}
