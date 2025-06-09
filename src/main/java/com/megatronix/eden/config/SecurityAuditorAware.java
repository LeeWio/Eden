package com.megatronix.eden.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.megatronix.eden.pojo.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Component
@Slf4j
public class SecurityAuditorAware implements AuditorAware<String> {
  @Override
  public @NonNull Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

    Object object = authentication.getPrincipal();

    if (object instanceof CustomUserDetails) {
      return Optional.of(((CustomUserDetails) object).getUser().getId());
    }

    return Optional.empty();
  }
}
