package com.megatronix.eden.service.impl;

import com.megatronix.eden.constant.RabbitConstant;
import com.megatronix.eden.constant.RedisConstant;
import com.megatronix.eden.constant.RoleConstant;
import com.megatronix.eden.enums.ResultEnum;
import com.megatronix.eden.enums.UserStatusEnum;
import com.megatronix.eden.pojo.AuthUser;
import com.megatronix.eden.pojo.Role;
import com.megatronix.eden.pojo.User;
import com.megatronix.eden.pojo.UserAuthPayload;
import com.megatronix.eden.repository.RoleRepository;
import com.megatronix.eden.repository.UserRepository;
import com.megatronix.eden.service.IUserService;
import com.megatronix.eden.util.JwtUtil;
import com.megatronix.eden.util.RedisUtil;
import com.megatronix.eden.util.ResultResponse;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerRequest;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.megatronix.eden.constant.RabbitConstant;

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

  @Resource
  private RedisUtil redisUtil;

  @Resource
  private RabbitTemplate rabbitTemplate;

  @Override
  public ResultResponse<String> requestVerificationCode(String email) {
    if (!Validator.isEmail(email)) {
      return ResultResponse.error(ResultEnum.INVALID_EMAIL_FORMAT);
    }

    if (!userRepository.existsUserByEmail(email)) {
      return ResultResponse.error(ResultEnum.USER_NOT_FOUND);
    }

    String captcha = RandomUtil.randomNumbers(6);

    redisUtil.set(RedisConstant.CAPTCHA_PREFIX + email, captcha, 10, TimeUnit.MINUTES);

    Map<String, String> message = new HashMap<>();
    message.put("email", email);
    message.put("captcha", captcha);

    rabbitTemplate.convertAndSend(RabbitConstant.CAPTCHA_EXCHANGE, RabbitConstant.CAPTCHA_ROUTING_KEY, message);

    return ResultResponse.success(ResultEnum.SUCCESS, "OTP sent successfully.");
  }

  // @Override
  // public ResultResponse<AuthUser> validateCaptcha(String email, String
  // verificationCode) {
  //
  // if (!Validator.isEmail(email)) {
  // return ResultResponse.error(ResultEnum.INVALID_EMAIL_FORMAT);
  // }
  //
  // if (!userRepository.existsUserByEmail(email)) {
  // return ResultResponse.error(ResultEnum.USER_NOT_FOUND);
  // }
  //
  // String captcha = redisUtil.get(RedisConstant.CAPTCHA_PREFIX + email,
  // String.class).orElse(null);
  //
  // if (StrUtil.isBlank(captcha)) {
  // return ResultResponse.error(ResultEnum.INVALID_CAPTCHA);
  // }
  //
  // if (StrUtil.equals(verificationCode, captcha)) {
  //
  // User user = userRepository.findUserByemail(email)
  // .orElseThrow(() -> new
  // UsernameNotFoundException(ResultEnum.USER_NOT_FOUND.getMessage()));
  //
  // Authentication authentication = new
  // UsernamePasswordAuthenticationToken(user.getEmail(), null);
  //
  // SecurityContextHolder.getContext().setAuthentication(authentication);
  //
  // String authorization = jwtUtil.generateAuthorization(authentication);
  //
  // return ResultResponse.success(ResultEnum.SUCCESS,
  // new AuthUser(user.getId(), user.getUsername(), user.getEmail(),
  // authorization,
  // Optional.of(user.getAvatar())));
  // }
  // return ResultResponse.error(ResultEnum.INVALID_CAPTCHA);
  // }

  @Override
  public ResultResponse<AuthUser> authenticateUser(UserAuthPayload userAuthPayload,
      HttpServletRequest httpServerRequest) {
    String clientIp = httpServerRequest.getRemoteAddr();

    String forwardedFor = httpServerRequest.getHeader("X-Forwarded-For");
    if (forwardedFor != null) {
      clientIp = forwardedFor.split(",")[0];
    }

    if (!Validator.isEmail(userAuthPayload.getEmail())) {
      return ResultResponse.error(ResultEnum.INVALID_EMAIL_FORMAT);
    }

    Authentication authentication = authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(userAuthPayload.getEmail(), userAuthPayload.getPassword()));

    User user = userRepository.findUserByemail(userAuthPayload.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException(ResultEnum.USER_NOT_FOUND.getMessage()));

    user.setLastLoginIp(clientIp);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String authorization = jwtUtil.generateAuthorization(authentication);

    return ResultResponse.success(ResultEnum.SUCCESS,
        new AuthUser(user.getId(), user.getUsername(), user.getEmail(), authorization,
            Optional.of(user.getAvatar()), user.getLastLoginIp()));

  }

  @Override
  @Transactional
  public ResultResponse<AuthUser> createAccount(UserAuthPayload userAuthPayload, HttpServletRequest httpServerRequest) {

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

    return authenticateUser(userAuthPayload, httpServerRequest);
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

  @Override
  public ResultResponse<String> updateLastLoginIp(String uid, String ip) {
    Optional<User> optionalUser = userRepository.findById(uid);
    if (optionalUser.isEmpty()) {
      return ResultResponse.error(ResultEnum.USER_NOT_FOUND);
    }

    User user = optionalUser.get();
    user.setLastLoginIp(ip);
    userRepository.save(user);

    return ResultResponse.success(ResultEnum.SUCCESS, "Last login IP updated");
  }
}
