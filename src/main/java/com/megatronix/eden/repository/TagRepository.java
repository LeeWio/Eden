package com.megatronix.eden.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megatronix.eden.pojo.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
}
