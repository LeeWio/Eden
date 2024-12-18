package com.megatronix.eden.pojo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.megatronix.eden.enums.UserStatusEnum;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
  private User user;
  private final Collection<? extends GrantedAuthority> authorities;

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return user.getStatus() != UserStatusEnum.BANNED;
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.getStatus() != UserStatusEnum.BANNED;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // 如果 status 是 4，则凭据已过期
    return user.getStatus() != UserStatusEnum.BANNED;
  }

  @Override
  public boolean isEnabled() {
    // 只有 status 是 0 时，账号才启用
    return user.getStatus() == UserStatusEnum.ACTIVE;
  }
}
