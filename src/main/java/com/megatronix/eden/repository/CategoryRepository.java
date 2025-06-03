package com.megatronix.eden.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megatronix.eden.pojo.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}
