package com.megatronix.eden.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megatronix.eden.pojo.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findRoleByName(String name);

  Optional<Set<Role>> findRolesByName(String name);
}
