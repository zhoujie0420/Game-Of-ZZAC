package com.kob.backend.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName : OssUtil  //类名
 * @Description :   //描述
 * @Author : dell //作者
 * @Date: 2023/5/21  20:09
 */
@Component
public class OssUtil implements InitializingBean {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyID;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accesskeySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = accessKeyID;
        ACCESS_KEY_SECRET = accesskeySecret;
        BUCKET_NAME = bucketName;
    }
}