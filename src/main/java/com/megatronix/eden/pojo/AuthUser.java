package com.megatronix.eden.pojo;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthUser {
  private String uid;
  private String username;
  private String email;
  private String authorization;
  private Optional<String> avatarUrl;
}
