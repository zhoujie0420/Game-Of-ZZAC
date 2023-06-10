package com.kob.backend.service.impl;
/**
 * @author zhouj
 * @create 2023/6/10 17:35
 */

import org.springframework.web.multipart.MultipartFile;

/**
 * oss上传管理Service
 */
public interface IOssService {
    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    String uploadImages(MultipartFile file);

    /**
     * 删除图片
     *
     * @param fileUrl
     * @return
     */
    boolean deleteImages(String fileUrl);


    String uploadFile(MultipartFile file);
}