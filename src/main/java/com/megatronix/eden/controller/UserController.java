package com.megatronix.eden.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.megatronix.eden.enums.ResultEnum;
import com.megatronix.eden.pojo.AuthUser;
import com.megatronix.eden.pojo.UserAuthPayload;
import com.megatronix.eden.service.IUserService;
import com.megatronix.eden.util.ResultResponse;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {
  @Resource
  private IUserService userService;

  @GetMapping("/hello")
  public ResultResponse<String> getHello() {
    return ResultResponse.error(ResultEnum.SUCCESS);
  }

  @PostMapping("/authenticate")
  public ResultResponse<AuthUser> authenticateUser(@RequestBody UserAuthPayload userAuthPayload) {
    return userService.authenticateUser(userAuthPayload);
  }

  @PostMapping
  public ResultResponse<String> createAccount(@RequestBody UserAuthPayload userAuthPayload) {
    return userService.createAccount(userAuthPayload);
  }
}
