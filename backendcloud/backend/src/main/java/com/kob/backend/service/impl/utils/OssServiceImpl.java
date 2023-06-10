package com.kob.backend.service.impl.utils;

/**
 * @author zhouj
 * @create 2023/6/10 17:32
 */


import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.kob.backend.service.impl.IOssService;
import com.kob.backend.utils.OssUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.kob.backend.utils.OssUtil.*;


/**
 * oss上传管理Service实现类
 */
@Service
public class OssServiceImpl implements IOssService {

    @Override  // 上传文件接口 ，返回上传的url存到数据库 ， 调用直接访问
    public String uploadFile(MultipartFile file) {
        String url = null;
        String endpoint = END_POINT;
        String accessKeyId = ACCESS_KEY_ID;
        String accessKeySecret = ACCESS_KEY_SECRET;
        String bucketName = OssUtil.BUCKET_NAME;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            InputStream inputStream = file.getInputStream();
            //获取上传的文件的名字
            String filename = file.getOriginalFilename();
            //随机uuid是为了拼接文件名，防止用户上传两个名字相同的文件后覆盖掉前一个
            UUID uuid = UUID.randomUUID();
            //这里是为了按上传时间分配目录。精确到月
            String s = DateTime.now().toString("yyyy/MM/");
            //拼接成完整的文件名。
            filename = s + uuid + filename;
            //传入三个参数
            ossClient.putObject(bucketName, filename, inputStream);
            //拼接url
            url = "https://" + bucketName + "." + endpoint + "/" + filename + "/image/jpg";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return url;
    }


    /**
     * 上传图片到oss
     *
     * @param file 图片文件
     * @return
     */
    @Override
    public String uploadImages(MultipartFile file) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ACCESS_KEY_ID;
        String accessKeySecret = ACCESS_KEY_SECRET;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流
        try {
            // 获取文件的名称
            String fileName = file.getOriginalFilename();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            // 调用oss的方法实现长传
            // 第一个参数 bucketName
            // 第二个参数 上传到oss的文件路径和文件名称
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(file.getBytes()), objectMetadata);
            // 关闭OSSClient。
            ossClient.shutdown();
            // 把上传的文件路径返回 （手动拼接）
            // 这里设置图片有效时间 我设置了30年
            Date expiration = new Date(System.currentTimeMillis() + 946080000 * 1000);
            String url = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除图片
     *
     * @param fileUrl 图片路径
     * @return
     */
    @Override
    public boolean deleteImages(String fileUrl) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ACCESS_KEY_ID;
        String accessKeySecret = ACCESS_KEY_SECRET;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = BUCKET_NAME;
        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String imgFile = fileUrl.replace("https://bucket-ans.oss-cn-hangzhou.aliyuncs.com/", "");
        String fileName = imgFile.substring(0, imgFile.indexOf("?"));

        // 根据BucketName,objectName删除文件
        ossClient.deleteObject(bucketName, fileName);
        ossClient.shutdown();
        return true;
    }

    // 实现图片的预览功能
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }

}