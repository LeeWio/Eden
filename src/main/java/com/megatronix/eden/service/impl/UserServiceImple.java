package com.megatronix.eden.service.impl;

import org.springframework.stereotype.Service;

import com.megatronix.eden.enums.ResultEnum;
import com.megatronix.eden.pojo.AuthUser;
import com.megatronix.eden.pojo.UserAuthPayload;
import com.megatronix.eden.repository.UserRepository;
import com.megatronix.eden.service.IUserService;
import com.megatronix.eden.util.ResultResponse;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImple implements IUserService {
  @Resource
  private UserRepository userRepository;

  @Override
  public ResultResponse<AuthUser> authenticateUser(UserAuthPayload userAuthPayload) {
    return ResultResponse.success(ResultEnum.SUCCESS, null);
  }

}
