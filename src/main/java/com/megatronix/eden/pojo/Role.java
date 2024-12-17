package com.megatronix.eden.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table("role")
@Setter
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Role implements Serializable {
  @Transient
  public static final Long SERIAL_VERSION_RID = -6849794870754623710L;

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

  @JsonIgnoreProperties(value = { "roles" })
  @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER, mappedBy = "roles")
  private Set<User> users;

  @JsonIgnoreProperties(value = { "roles" })
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "roles")
  private Set<Permission> permissions;
}
