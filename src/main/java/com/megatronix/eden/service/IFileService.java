package com.megatronix.eden.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.megatronix.eden.enums.FileTypeEnum;
import com.megatronix.eden.pojo.File;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Service
@Tag(name = "Upload multiple files", description = "Uploads multiple files and stores their metadata. Supports optional file type classification.")
public interface IFileService {

  /**
   * Uploads a single file with an optional file type classification.
   *
   * @param multipartFile The file to be uploaded.
   * @param type          The type of the file (e.g., IMAGE, VIDEO, etc.).
   * @return A ResultResponse containing the uploaded file metadata.
   * @throws IOException If an I/O error occurs during file processing.
   */
  @Operation(summary = "Upload a single file", description = "Uploads a single file and stores its metadata. Supports optional file type classification.")
  ResultResponse<File> uploadFile(
      @Parameter(description = "The file to be uploaded", required = true) MultipartFile multipartFile,
      @Parameter(description = "Optional file type classification") FileTypeEnum type) throws IOException;

  /**
   * Uploads multiple files with an optional file type classification.
   *
   * @param multipartFiles An array of files to be uploaded.
   * @param type           The type of the files (e.g., IMAGE, VIDEO, etc.).
   * @return A ResultResponse containing a list of uploaded file metadata.
   * @throws IOException If any I/O error occurs during file processing.
   */
  @Operation(summary = "Upload multiple files", description = "Uploads multiple files and stores their metadata. Supports optional file type classification.")
  ResultResponse<List<File>> uploadFiles(
      @Parameter(description = "Array of files to be uploaded", required = true) MultipartFile[] multipartFiles,
      @Parameter(description = "Optional file type classification") FileTypeEnum type) throws IOException;

  /**
   * Retrieves a file's metadata by its unique ID.
   *
   * @param id The unique ID of the file.
   * @return A ResultResponse containing the file metadata.
   */
  @Operation(summary = "Get file by ID", description = "Retrieves a file's metadata based on its unique identifier.")
  ResultResponse<File> getFileById(
      @Parameter(description = "The unique ID of the file", required = true) String id);
}
