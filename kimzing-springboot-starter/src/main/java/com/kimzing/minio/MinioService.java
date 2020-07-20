package com.kimzing.minio;

import com.kimzing.autoconfigure.properties.MinioProperties;
import com.kimzing.utils.exception.ExceptionManager;
import com.kimzing.utils.log.LogUtil;
import com.kimzing.utils.string.StringUtil;
import io.minio.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Minio文件存储服务.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/20 01:10
 */
public class MinioService {

    public static final String MINIO_ERROR_CODE = "MINIO";

    private MinioClient minioClient;

    private MinioProperties minioProperties;

    public MinioService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    /**
     * 判断存储桶是否存在
     *
     * @param bucket
     */
    public Boolean bucketExists(String bucket) {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());
            return bucketExists;
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "判断存储桶是否存在时发生异常:" + e.getMessage());
        }
    }

    /**
     * 创建存储桶
     *
     * @param bucket
     */
    public void makeBucket(String bucket) {
        if (StringUtil.isBlank(bucket)) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "存储桶名称不能为空");
        }

        Boolean exists = bucketExists(bucket);
        if (exists) {
            LogUtil.debug("存储桶[{}]已存在", bucket);
            return;
        }

        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
            LogUtil.info("存储桶[{}]创建成功", bucket);
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "存储桶创建异常:" + e.getMessage());
        }
    }

    /**
     * 设置存储桶策略为公共读
     *
     * @param bucket
     */
    public void setBucketPolicyToReadOnly(String bucket) {
        try {
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(getPolicy(bucket))
                    .build());
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "存储桶策略异常:" + e.getMessage());
        }
    }

    /**
     * 手动设置存储桶策略
     *
     * @param bucket
     * @param config
     */
    public void setBucketPolicy(String bucket, String config) {
        try {
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                .bucket(bucket)
                .config(config)
                .build());
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "存储桶策略异常:" + e.getMessage());
        }
    }

    /**
     * Bucket公共读的配置
     * @param bucket
     * @return
     */
    private String getPolicy(String bucket) {
        return String.format("{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::%s\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::%s/*\"]}]}",
        bucket, bucket);
    }

    /**
     * 删除存储桶
     *
     * @param bucket
     */
    public void removeBucket(String bucket) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "删除存储桶异常:" + e.getMessage());
        }
    }

    /**
     * 通过MultipartFile上传文件
     *
     * @param bucket 存储桶
     * @return
     */
    public MinioObjectInfo upload(String bucket, String path, MultipartFile multipartFile) {
        try {
            return upload(bucket, path, multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getInputStream());
        } catch (IOException e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "获取上传文件流异常");
        }
    }

    /**
     * 上传文件
     *
     * @param bucket      存储桶名称
     * @param path        相对于存储桶的路径  可以为空
     * @param fileName    存储对象名
     * @param contentType 文件类型
     * @param inputStream 文件流
     * @return
     */
    public MinioObjectInfo upload(String bucket, String path, String fileName, String contentType, InputStream inputStream) {
        if (StringUtil.isBlank(bucket)) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "存储桶不能为空");
        }
        if (StringUtil.isBlank(fileName)) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "对象名不能为空");
        }
        if (StringUtil.isBlank(contentType)) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "内容类型不能为空");
        }

        try {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", contentType);
            PutObjectOptions options = new PutObjectOptions(inputStream.available(), -1);
            options.setHeaders(headers);
            fileName = getPath(path) + getPrefix() + fileName;
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            String url = minioClient.getObjectUrl(bucket, fileName);
            return new MinioObjectInfo()
                    .setBucket(bucket)
                    .setPath(path)
                    .setName(fileName)
                    .setContentType(contentType)
                    .setUrl(url);
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "上传文件异常:" + e.getMessage());
        }
    }

    private String getPath(String path) {
        if (StringUtil.isBlank(path)) {
            return "";
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 删除存储对象
     *
     * @param bucket
     * @param name
     */
    public void removeObject(String bucket, String name) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(name)
                    .build());
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "删除文件异常:" + e.getMessage());
        }
    }

    /**
     * 获取文件对象流, 并且需要制定前缀，如果没有前缀可以指定为空
     */
    public InputStream getObject(String bucket, String name) {
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(name)
                    .build());
            return inputStream;
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "获取文件异常:" + e.getMessage());
        }
    }

    private String getPrefix() {
        if (!StringUtil.isBlank(minioProperties.getPrefix())) {
            return minioProperties.getPrefix();
        }
        if (!StringUtil.isBlank(minioProperties.getPrefixType()) && minioProperties.getPrefixType().equals("time")) {
            if (StringUtil.isBlank(minioProperties.getTimePattern())) {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            }
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(minioProperties.getTimePattern()));
        }
        return "";
    }


}
