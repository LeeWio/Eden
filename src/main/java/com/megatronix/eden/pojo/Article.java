package com.megatronix.eden.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.megatronix.eden.enums.ArticleStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "article")
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "Represents an article in the blogging system.")
public class Article implements Serializable {
  @Transient
  private static final long serialVersionUID = -6249794470754667710L;
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique identifier for the article.", example = "123e4567-e89b-12d3-a456-426614174000")
  private String id;

  @Column(name = "title", nullable = false, length = 255)
  @Schema(description = "Title of the article.", example = "Introduction to Java")
  private String title;

  @Column(name = "slug", unique = true, length = 255)
  @Schema(description = "URL-friendly identifier for the article.", example = "introduction-to-java")
  private String slug;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  @Schema(description = "Full content of the article.")
  private String content;

  @Column(name = "summary", length = 500)
  @Schema(description = "Short summary of the article.", example = "A beginner's guide to Java programming.")
  private String summary;

  @Column(name = "status", nullable = false)
  @Enumerated(value = EnumType.ORDINAL)
  @Schema(description = "Current status of the article.", example = "PUBLISHED")
  private ArticleStatusEnum status = ArticleStatusEnum.REVIEW;

  @CreatedBy
  @LastModifiedBy
  @Column(name = "author_id", nullable = false)
  @Schema(description = "Unique identifier for the author of the article.", example = "123e4567-e89b-12d3-a456-426614174001")
  private String authorId;

  @Column(name = "thumbnail_url", length = 500)
  @Schema(description = "URL of the thumbnail image for the article.", example = "https://example.com/image.jpg")
  private String thumbnailUrl;

  @Column(name = "views", nullable = false)
  @Schema(description = "Number of times the article has been viewed.", example = "100")
  private Integer views = 0;

  @Column(name = "likes", nullable = false)
  @Schema(description = "Number of likes the article has received.", example = "50")
  private Integer likes = 0;

  @Column(name = "is_featured", nullable = false)
  @Schema(description = "Indicates if the article is featured.", example = "true")
  private Boolean isFeatured = false;

  @Column(name = "create_at", nullable = false, updatable = false)
  @CreatedDate
  @Schema(description = "Date and time when the article was created.", example = "2025-01-01T12:00:00Z")
  private Date createAt;

  @Column(name = "update_at", nullable = false)
  @LastModifiedDate
  @Schema(description = "Date and time when the article was last updated.", example = "2025-01-02T15:00:00Z")
  private Date updateAt;

  @ManyToMany
  @JsonIgnoreProperties(value = { "articles" })
  @JoinTable(name = "article_tag", joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
  @Schema(description = "Tags associated with the article.")
  private Set<Tag> tags;

  @ManyToMany
  @JsonIgnoreProperties(value = { "articles" })
  @JoinTable(name = "article_category", joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
  @Schema(description = "Categories associated with this article.")
  private Set<Category> categories;
}
