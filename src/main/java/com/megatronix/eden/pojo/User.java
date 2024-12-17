package com.megatronix.eden.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.megatronix.eden.enums.GenderEnum;
import com.megatronix.eden.enums.UserStatusEnum;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Table("user")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

  @Transient
  private static final Long SERIAL_VERSION_UID = -6249794470754667710L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "username")
  private String username;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "avatar")
  private String avatar = "https://images.pexels.com/photos/24491299/pexels-photo-24491299.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";

  @Column(name = "motto")
  private String motto = "Work hard, paly hard.";

  @Column(name = "gender")
  @Enumerated(EnumType.ORDINAL)
  private GenderEnum gender = GenderEnum.PREFER_NOT_TO_SAY;

  @Column(name = "status")
  @Enumerated(EnumType.ORDINAL)
  private UserStatusEnum status = UserStatusEnum.PENDING;

  @Column(name = "birth_date")
  private Date birthDate;

  @Column(name = "create_at")
  @CreatedDate
  private Date createAt;

  @Column(name = "update_at")
  @LastModifiedDate
  private Date updateAt;

  @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
  @JoinTable(
    name = "user_role"
    joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
  )
  private Set<Role> roles;

}
