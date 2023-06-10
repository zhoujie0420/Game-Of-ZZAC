package com.kob.backend.controller;/**
 * @author zhouj
 * @create 2023/6/10 17:37
 * @ClassName : OssController  //类名
 * @Description :   //描述
 * @Author : dell //作者
 * @Date: 2023/6/10  17:37
 * @ClassName : OssController  //类名
 * @Description :   //描述
 * @Author : dell //作者
 * @Date: 2023/6/10  17:37
 * @ClassName : OssController  //类名
 * @Description :   //描述
 * @Author : dell //作者
 * @Date: 2023/6/10  17:37
 */

/**
 * @ClassName : OssController  //类名
 * @Description :   //描述
 * @Author : dell //作者
 * @Date: 2023/6/10  17:37
 */

import com.kob.backend.service.impl.utils.OssServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Oss相关操作接口
 */
@RestController
@Api(tags = "OssController", description = "Oss管理")
@RequestMapping("/api/aliyun/oss")
public class OssController {
    @Autowired
    private OssServiceImpl ossService;

    @PostMapping("/upload")
    public String upload(@RequestPart("file") MultipartFile file) {
        String url = ossService.uploadFile(file);
        return url;
    }


    @PostMapping("/uploadImages")
    public String uploadImages(@RequestPart("file") MultipartFile file) {
        // 获取上传头像的文件 MultipartFile
        // 返回上传的oss路径
        String url = ossService.uploadImages(file);
        return url;
    }


}