package com.megatronix.eden.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "category")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "Category entity representing a grouping of articles")
public class Category implements Serializable {

  private static final long serialVersionUID = -6249794470754667710L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique identifier for the category", example = "550e8400-e29b-41d4-a716-446655440000")
  private String id;

  @Column(name = "name", nullable = false, unique = true)
  @Schema(description = "Name of the category", example = "Technology")
  private String name;

  @Column(name = "description", length = 500)
  @Schema(description = "Description of the category", example = "A category dedicated to technology-related topics")
  private String description;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  @Schema(description = "Timestamp when the category was created", example = "2024-01-01T10:00:00Z")
  private Date createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  @Schema(description = "Timestamp when the category was last updated", example = "2024-01-02T12:00:00Z")
  private Date updatedAt;

  @ManyToMany
  @JoinTable(name = "article_category", joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"))
  @Schema(description = "Articles under this category.")
  private Set<Article> articles;
}
