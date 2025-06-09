package com.megatronix.eden.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megatronix.eden.pojo.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

}
