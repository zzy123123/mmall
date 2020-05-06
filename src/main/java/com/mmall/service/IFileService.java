package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created By Zzyy
 **/
public interface IFileService {

    String upload(MultipartFile file, String path);
}
