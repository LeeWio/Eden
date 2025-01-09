package com.megatronix.eden.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Table(name = "tag")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Setter
@Getter
@Schema(description = "Tag entity representing a category or label used to organize articles")
public class Tag implements Serializable {

  @Transient
  private static final long SERIAL_VERSION_UID = -6249794470751667210L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique identifier for the tag", example = "550e8400-e29b-41d4-a716-446655440000")
  private String id;

  @Column(name = "name", nullable = false, unique = true)
  @Schema(description = "Name of the tag", example = "Technology")
  private String name;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  @Schema(description = "Timestamp when the tag was created", example = "2024-01-01T10:00:00Z")
  private Date createdAt;
  //
  // @LastModifiedDate
  // @Column(name = "updated_at", nullable = false)
  // @Schema(description = "Timestamp when the tag was last updated", example =
  // "2024-01-02T12:00:00Z")
  // private Date updatedAt;

  @ManyToMany(mappedBy = "tags")
  @Schema(description = "Articles associated with the tag.")
  private Set<Article> articles;
}
