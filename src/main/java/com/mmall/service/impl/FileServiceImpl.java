package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import com.mmall.service.IFileService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created By Zzyy
 **/
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
        String uploadFileName= UUID.randomUUID().toString()+fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名：{}，上传的路径：{}，新文件名：{}",fileName,path,uploadFileName+fileExtensionName);

        File fileDir =new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile =new File(path,uploadFileName);
       try {
           //上传文件
           file.transferTo(targetFile);
           //将targetFile 上传到我们的FTP服务器上
           FTPUtil.uploadFile(Lists.newArrayList(targetFile));
           //上传完之后，删除upload文件
           targetFile.delete();
       }catch (IOException e){
           logger.error("上传文件异常",e);
            return null;
       }
        return targetFile.getName();
    }


}
