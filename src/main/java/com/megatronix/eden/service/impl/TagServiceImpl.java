package com.megatronix.eden.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.megatronix.eden.enums.ResultEnum;
import com.megatronix.eden.pojo.Tag;
import com.megatronix.eden.repository.TagRepository;
import com.megatronix.eden.service.ITagService;
import com.megatronix.eden.util.ResultResponse;

import jakarta.annotation.Resource;

@Service
public class TagServiceImpl implements ITagService {
  @Resource
  private TagRepository tagRepository;

  @Override
  public ResultResponse<String> create(Tag tag) {
    tagRepository.save(tag);
    return ResultResponse.success(ResultEnum.SUCCESS, "adf");
  }

  @Override
  public ResultResponse<List<Tag>> get() {
    return ResultResponse.success(ResultEnum.SUCCESS, tagRepository.findAll());
  }
}
