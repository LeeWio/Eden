package com.megatronix.eden.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
  private static final String JWT_SECRET = "daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb";

  private JWTSigner getJwtSigner() {
    return JWTSignerUtil.hs256(JWT_SECRET.getBytes());
  }

  public String generateAuthorization(Authentication authentication) {
    Map<String, Object> map = new HashMap<String, Object>() {
      {
        put("email", authentication.getName());
        put("iat", DateUtil.date());
        put("nbf", DateUtil.offsetDay(DateUtil.date(), 1));
        put("ext", DateUtil.offsetDay(DateUtil.date(), 7));
      }
    };

    return JWTUtil.createToken(map, getJwtSigner());
  }

  public String getEmailFromAuthorization(String authorization) {
    JWT jwt = JWTUtil.parseToken(authorization);
    return (String) jwt.getPayload("email");
  }

  public boolean validateAuthorization(String authorization) {
    return validateSignature(authorization) && validateDate(authorization);
  }

  private boolean validateSignature(String authorization) {
    return JWTUtil.verify(authorization, getJwtSigner());
  }

  private boolean validateDate(String authorization) {
    try {
      JWTValidator.of(authorization).validateDate(DateUtil.date());
      return true;
    } catch (Exception e) {
      log.error("Date validation failed: {}", e.getMessage());
      return false;
    }
  }
}
