package com.megatronix.eden.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Table(name = "file")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Schema(name = "File", description = "File entity that stores uploaded file metadata")
public class File implements Serializable {

  @Transient
  private static final Long SERIAL_VERSION_UID = -2979586104467190101L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique identifier for the file")
  private String id;

  @Column(name = "original_name", nullable = false)
  @Schema(description = "Original name of the uploaded file")
  private String originalName;

  @Column(name = "file_url", nullable = false)
  @Schema(description = "Publicly accessible URL of the file")
  private String fileUrl;

  @Column(name = "content_type")
  @Schema(description = "MIME type of the file, e.g., image/png, application/pdf")
  private String contentType;

  @Column(name = "file_size")
  @Schema(description = "Size of the file in bytes")
  private Long fileSize;

  @Column(name = "file_type")
  @Schema(description = "Custom file type tag, used for business classification")
  private String fileType;

  @Column(name = "create_at")
  @CreatedDate
  @Schema(description = "Timestamp when the file was uploaded")
  private Date createAt;

  @Column(name = "update_at")
  @LastModifiedDate
  @Schema(description = "Timestamp when the file metadata was last updated")
  private Date updateAt;
}
