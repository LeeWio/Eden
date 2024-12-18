package com.megatronix.eden.service;

import org.springframework.stereotype.Service;

import com.megatronix.eden.pojo.AuthUser;
import com.megatronix.eden.pojo.UserAuthPayload;
import com.megatronix.eden.util.ResultResponse;

@Service
public interface IUserService {
  ResultResponse<AuthUser> authenticateUser(UserAuthPayload userAuthPayload);

  ResultResponse<String> createAccount(UserAuthPayload userAuthPayload);
}
