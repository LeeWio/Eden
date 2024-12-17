package com.megatronix.eden.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megatronix.eden.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Boolean existsUserByEmail(String email);

  Optional<User> findUserByemail(String email);
}
