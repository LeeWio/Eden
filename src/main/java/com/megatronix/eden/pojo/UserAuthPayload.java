package com.megatronix.eden.pojo;

import java.util.Date;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAuthPayload {
  private Optional<String> username;
  private Optional<String> bio;
  private Optional<String> avatarUrl;
  private Optional<String> phoneNumber;
  private Optional<Date> dateOfBirth;
  private String email;
  private String password;
}
