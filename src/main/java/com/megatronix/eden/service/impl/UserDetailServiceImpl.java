package com.megatronix.eden.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.megatronix.eden.pojo.User;
import com.megatronix.eden.repository.UserRepository;

import jakarta.annotation.Resource;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
  @Resource
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByemail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    return null;
  }
}
