package com.megatronix.eden.controller;

import com.megatronix.eden.util.ResultResponse;
import com.megatronix.eden.enums.FileUploadEnum;

import lombok.Data;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileUploadController {

  @PostMapping("/upload")
  public ResultResponse<List<FileUploadResponse>> uploadFiles(
      @RequestParam("files") List<MultipartFile> files,
      @RequestParam(value = "type", required = false) FileUploadEnum type

  ) {
    return null;

  }
}

@Data
class FileUploadResponse {
  private String fileName;
  private String fileUrl;
}
