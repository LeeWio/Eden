package com.megatronix.eden.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "permission")
@Data
public class Permission implements Serializable {
  @Transient
  private static final Long SERIAL_VERSION_PID = -6849794870754623710L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @CreatedDate
  @Column(name = "create_at")
  private Date createAt;

  @LastModifiedDate
  @Column(name = "update_at")
  private Date updateAt;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles;
}
