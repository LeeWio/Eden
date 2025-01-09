package com.megatronix.eden.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "role")
@Setter
@Getter
@ToString(exclude = { "users", "permissions" })
@EntityListeners(AuditingEntityListener.class)
public class Role implements Serializable {
  @Transient
  public static final long SERIAL_VERSION_RID = -6849794870754623710L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "create_at")
  private Date createAt;

  @Column(name = "update_at")
  private Date updateAt;

  @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER, mappedBy = "roles")
  private Set<User> users;

  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "roles")
  private Set<Permission> permissions;
}
