package com.megatronix.eden.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.megatronix.eden.enums.GenderEnum;
import com.megatronix.eden.enums.UserStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Table(name = "user")
@Data
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Schema(name = "User", description = "User entity that contains all user-related information")
public class User implements Serializable {

  @Transient
  private static final long SERIAL_VERSION_UID = -6249794470754667710L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique identifier for the user")
  private String id;

  @Column(name = "username")
  @Schema(description = "Username of the user")
  private String username;

  @Column(name = "email", unique = true, nullable = false)
  @Schema(description = "Email address of the user", example = "user@example.com")
  private String email;

  @Column(name = "password")
  @Schema(description = "Password for the user account")
  private String password;

  @Column(name = "phone_number")
  @Schema(description = "Phone number of the user", example = "+1234567890")
  private String phoneNumber;

  @Column(name = "avatar")
  @Schema(description = "Avatar URL of the user", defaultValue = "https://images.pexels.com/photos/24491299/pexels-photo-24491299.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
  private String avatar = "https://images.pexels.com/photos/24491299/pexels-photo-24491299.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";

  @Column(name = "motto")
  @Schema(description = "User's motto", defaultValue = "Work hard, play hard.")
  private String motto = "Work hard, paly hard.";

  @Column(name = "gender")
  @Enumerated(EnumType.ORDINAL)
  @Schema(description = "Gender of the user", allowableValues = { "PREFER_NOT_TO_SAY", "MALE", "FEMALE" })
  private GenderEnum gender = GenderEnum.PREFER_NOT_TO_SAY;

  @Column(name = "status")
  @Enumerated(EnumType.ORDINAL)
  @Schema(description = "Status of the user", allowableValues = { "PENDING", "ACTIVE", "INACTIVE" })
  private UserStatusEnum status = UserStatusEnum.PENDING;

  @Column(name = "birth_date")
  @Schema(description = "Birth date of the user")
  private Date birthDate;

  @Column(name = "create_at")
  @CreatedDate
  @Schema(description = "Date and time when the user was created")
  private Date createAt;

  @Column(name = "update_at")
  @LastModifiedDate
  @Schema(description = "Date and time when the user was last updated")
  private Date updateAt;

  @Schema(description = "Roles assigned to the user")
  @JsonIgnoreProperties(value = { "users" })
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles;

}
