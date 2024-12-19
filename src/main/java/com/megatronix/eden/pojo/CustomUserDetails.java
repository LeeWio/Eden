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

  /**
   * Determines if the account is expired.
   * The account is considered expired if the status is INACTIVE (not activated).
   * 
   * @return true if the account is not expired
   */
  @Override
  public boolean isAccountNonExpired() {
    return user.getStatus() != UserStatusEnum.INACTIVE;
  }

  /**
   * Determines if the account is locked.
   * The account is considered locked if the status is BANNED (disabled).
   * 
   * @return true if the account is not locked
   */
  @Override
  public boolean isAccountNonLocked() {
    return user.getStatus() != UserStatusEnum.BANNED;
  }

  /**
   * Determines if the credentials (password) are expired.
   * Credentials are considered expired if the account status is BANNED (disabled)
   * or DELETED.
   * 
   * @return true if the credentials are not expired
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return user.getStatus() != UserStatusEnum.BANNED && user.getStatus() != UserStatusEnum.DELETED;
  }

  /**
   * Determines if the account is enabled.
   * The account is enabled only if the status is ACTIVE.
   * 
   * @return true if the account is enabled
   */
  @Override
  public boolean isEnabled() {
    return user.getStatus() == UserStatusEnum.ACTIVE;
  }
}
