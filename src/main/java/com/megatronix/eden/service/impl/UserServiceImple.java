package com.megatronix.eden.service.impl;

import java.lang.module.ModuleDescriptor.Exports;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.megatronix.eden.constant.RoleConstant;
import com.megatronix.eden.enums.ResultEnum;
import com.megatronix.eden.enums.UserStatusEnum;
import com.megatronix.eden.pojo.AuthUser;
import com.megatronix.eden.pojo.Role;
import com.megatronix.eden.pojo.UserAuthPayload;
import com.megatronix.eden.repository.RoleRepository;
import com.megatronix.eden.repository.UserRepository;
import com.megatronix.eden.service.IUserService;
import com.megatronix.eden.util.JwtUtil;
import com.megatronix.eden.util.ResultResponse;
import com.megatronix.eden.pojo.User;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImple implements IUserService {

  @Resource
  private UserRepository userRepository;

  @Resource
  private PasswordEncoder passwordEncoder;

  @Resource
  private RoleRepository roleRepository;

  @Resource
  private AuthenticationManager authenticationManager;

  @Resource
  private JwtUtil jwtUtil;

  @Override
  public ResultResponse<AuthUser> authenticateUser(UserAuthPayload userAuthPayload) {

    if (!Validator.isEmail(userAuthPayload.getEmail())) {
      return ResultResponse.error(ResultEnum.INVALID_EMAIL_FORMAT);
    }

    Authentication authentication = authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(userAuthPayload.getEmail(), userAuthPayload.getPassword()));

    User user = userRepository.findUserByemail(userAuthPayload.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException(ResultEnum.USER_NOT_FOUND.getMessage()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String authorization = jwtUtil.generateAuthorization(authentication);

    return ResultResponse.success(ResultEnum.SUCCESS,
        new AuthUser(user.getId(), user.getUsername(), user.getEmail(), authorization,
            Optional.of(user.getAvatar())));

  }

  @Override
  @Transactional
  public ResultResponse<String> createAccount(UserAuthPayload userAuthPayload) {

    if (!Validator.isEmail(userAuthPayload.getEmail())) {
      return ResultResponse.error(ResultEnum.INVALID_EMAIL_FORMAT);
    }

    if (userRepository.existsUserByEmail(userAuthPayload.getEmail())) {
      return ResultResponse.error(ResultEnum.USER_EXIST);
    }

    User user = BeanUtil.copyProperties(userAuthPayload, User.class);
    if (StrUtil.isBlank(user.getUsername())) {
      user.setUsername(user.getEmail().split("@")[0]);
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Optional<Role> role = roleRepository.findRoleByName(RoleConstant.GUEST);

    if (role.isPresent()) {
      user.setRoles(Collections.singleton(role.orElse(null)));
    }

    userRepository.save(user);

    return ResultResponse.success(ResultEnum.SUCCESS, "User created successfully");
  }

  @Override
  public ResultResponse<String> setUserStatus(String uid, UserStatusEnum newUserStatusEnum) {

    User user = userRepository.findById(uid)
        .orElseThrow(() -> new UsernameNotFoundException(ResultEnum.USER_NOT_FOUND.getMessage()));

    UserStatusEnum userStatusEnum = user.getStatus();

    switch (userStatusEnum) {
      case PENDING:
        if (newUserStatusEnum != UserStatusEnum.ACTIVE && newUserStatusEnum != UserStatusEnum.BANNED) {
          return ResultResponse.error(ResultEnum.INVALID_STATUS_TRANSITION);
        }
        break;

      case ACTIVE:
        if (newUserStatusEnum == UserStatusEnum.PENDING || newUserStatusEnum == UserStatusEnum.DELETED) {
          return ResultResponse.error(ResultEnum.INVALID_STATUS_TRANSITION);
        }
        break;

      case BANNED:
        if (newUserStatusEnum != UserStatusEnum.ACTIVE && newUserStatusEnum != UserStatusEnum.DELETED) {
          return ResultResponse.error(ResultEnum.INVALID_STATUS_TRANSITION);
        }
        break;

      case DELETED:
        return ResultResponse.error(ResultEnum.INVALID_STATUS_TRANSITION);

      default:
        break;
    }

    user.setStatus(newUserStatusEnum);

    userRepository.save(user);

    return ResultResponse.success(ResultEnum.SUCCESS, "User account has been updated.");
  }

  @Override
  public ResultResponse<User> getUser(String uid) {
    try {

      User user = userRepository.findById(uid)
          .orElseThrow(() -> new UsernameNotFoundException(ResultEnum.USER_NOT_FOUND.getMessage()));
      return ResultResponse.success(ResultEnum.SUCCESS, user);

    } catch (UsernameNotFoundException e) {

      return ResultResponse.error(ResultEnum.USER_NOT_FOUND);

    }
  }

  @Override
  public ResultResponse<String> deleteUser(String uid) {
    if (userRepository.existsById(uid)) {
      this.setUserStatus(uid, UserStatusEnum.DELETED);
      return ResultResponse.success(ResultEnum.SUCCESS, "User has been successfully deleted.");
    } else {
      return ResultResponse.error(ResultEnum.USER_NOT_FOUND);
    }
  }

  @Override
  public ResultResponse<List<User>> getUsers(Pageable pageable) {
    try {

      Page<User> userPage = userRepository.findAll(pageable);

      if (userPage.isEmpty()) {
        return ResultResponse.error(ResultEnum.USER_NOT_FOUND);
      }

      return ResultResponse.success(ResultEnum.SUCCESS, userPage.getContent());

    } catch (Exception e) {
      return ResultResponse.error(ResultEnum.USER_NOT_FOUND);
    }
  }
}
