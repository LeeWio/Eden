
package com.megatronix.eden.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.megatronix.eden.enums.FileTypeEnum;
import com.megatronix.eden.enums.ResultEnum;
import com.megatronix.eden.pojo.File;
import com.megatronix.eden.repository.FileRepository;
import com.megatronix.eden.service.IFileService;
import com.megatronix.eden.util.ResultResponse;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements IFileService {

  @Resource
  private FileRepository fileRepository;

  @Value("${file.upload-dir:uploads}")
  private String uploadDir;

  @Override
  public ResultResponse<File> uploadFile(MultipartFile multipartFile, FileTypeEnum type) throws IOException {
    if (multipartFile.isEmpty()) {
      return ResultResponse.error(ResultEnum.FILE_NOT_FOUNT);
    }

    String originalName = multipartFile.getOriginalFilename();
    String extName = FileUtil.extName(originalName);
    String uuid = IdUtil.fastSimpleUUID();
    String fileName = uuid + (StrUtil.isNotBlank(extName) ? "." + extName : "");

    String rootPath = System.getProperty("user.dir");
    java.io.File savePath = FileUtil.mkdir(rootPath + java.io.File.separator + uploadDir);
    java.io.File destFile = new java.io.File(savePath, fileName);

    multipartFile.transferTo(destFile);

    String fileUrl = "/uploads/" + fileName;

    File fileEntity = new File();
    fileEntity.setOriginalName(originalName);
    fileEntity.setFileUrl(fileUrl);
    fileEntity.setContentType(multipartFile.getContentType());
    fileEntity.setFileSize(multipartFile.getSize());
    fileEntity.setFileType(type != null ? type : FileTypeEnum.OTHER);

    fileRepository.save(fileEntity);

    return ResultResponse.success(ResultEnum.SUCCESS, fileEntity);
  }

  @Override
  public ResultResponse<List<File>> uploadFiles(MultipartFile[] multipartFiles, FileTypeEnum type) throws IOException {
    if (multipartFiles == null || multipartFiles.length == 0) {
      return ResultResponse.error(ResultEnum.FILE_NOT_FOUNT);
    }

    List<File> savedFiles = new ArrayList<>();
    for (MultipartFile file : multipartFiles) {
      ResultResponse<File> result = uploadFile(file, type);
      if (result.getStatus() != ResultEnum.SUCCESS.getStatus()) {
        return ResultResponse.error(ResultEnum.FILE_NOT_UPLOAD);
      }
      savedFiles.add(result.getData());
    }

    return ResultResponse.success(ResultEnum.SUCCESS, savedFiles);
  }

  @Override
  public ResultResponse<File> getFileById(String id) {
    Optional<File> optionalFile = fileRepository.findById(id);
    if (optionalFile.isPresent()) {
      return ResultResponse.success(ResultEnum.SUCCESS, optionalFile.get());
    } else {
      return ResultResponse.error(ResultEnum.FILE_NOT_FOUNT);
    }
  }

}
