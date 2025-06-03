package com.megatronix.eden.controller;

import com.megatronix.eden.enums.FileTypeEnum;
import com.megatronix.eden.pojo.File;
import com.megatronix.eden.service.IFileService;
import com.megatronix.eden.util.ResultResponse;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileUploadController {

  @Resource
  private IFileService fileService;

  @PostMapping("/upload")
  public ResultResponse<File> uploadFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "type", required = false) FileTypeEnum type) throws IOException {
    return fileService.uploadFile(file, type);
  }

  @PostMapping("/uploads")
  public ResultResponse<List<File>> uploadFiles(
      @RequestParam("files") MultipartFile[] files,
      @RequestParam(value = "type", required = false) FileTypeEnum type) throws IOException {
    return fileService.uploadFiles(files, type);
  }

  @GetMapping("/{id}")
  public ResultResponse<File> getFileById(@PathVariable String id) {
    return fileService.getFileById(id);
  }
}
