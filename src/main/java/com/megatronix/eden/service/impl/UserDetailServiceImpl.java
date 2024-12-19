package com.megatronix.eden.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.megatronix.eden.pojo.CustomUserDetails;
import com.megatronix.eden.pojo.User;
import com.megatronix.eden.repository.UserRepository;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
  @Resource
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findUserByemail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

    Set<GrantedAuthority> authorities = user.getRoles().stream().flatMap(role -> {
      Set<GrantedAuthority> roleAuthorities = role.getPermissions().stream()
          .map(permission -> new SimpleGrantedAuthority(role.getName()))
          .collect(Collectors.toSet());

      roleAuthorities.add(new SimpleGrantedAuthority(role.getName()));

      return roleAuthorities.stream();

    }).collect(Collectors.toSet());
    return new CustomUserDetails(user, authorities);
  }
}
