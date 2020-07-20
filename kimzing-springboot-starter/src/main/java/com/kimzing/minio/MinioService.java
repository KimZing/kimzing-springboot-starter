package com.kimzing.minio;

import com.kimzing.utils.exception.ExceptionManager;
import com.kimzing.utils.log.LogUtil;
import com.kimzing.utils.string.StringUtil;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import com.kimzing.autoconfigure.properties.MinioProperties;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    public MinioService(MinioClient minioClient, MinioProperties minioProperties) throws IOException, InvalidKeyException, InvalidResponseException, BucketPolicyTooLargeException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        // 初始化存储桶
        if (!StringUtil.isBlank(minioProperties.getBucket())) {
            makeBucket(minioProperties.getBucket());
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

        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());
            if (bucketExists) {
                LogUtil.info("存储桶[{}]已存在", bucket);
            } else {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build());
                LogUtil.info("存储桶[{}]创建成功", bucket);
            }
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "存储桶创建异常:" + e.getMessage());
        }
    }

    /**
     * 删除存储桶
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
     * 通过MultipartFile上传文件， 使用服务配置的存储桶
     *
     * @return
     */
    public MinioObjectInfo upload(MultipartFile multipartFile) {
        return upload(minioProperties.getBucket(), multipartFile);
    }

    /**
     * 通过MultipartFile上传文件
     *
     * @param bucket
     * @return
     */
    public MinioObjectInfo upload(String bucket, MultipartFile multipartFile) {
        try {
            return upload(bucket, multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getInputStream());
        } catch (IOException e) {
            throw ExceptionManager.createByCodeAndMessage("UPLOAD", "获取上传文件流异常");
        }
    }

    /**
     * 上传文件，使用服务配置的存储桶
     *
     * @param fileName
     * @param contentType
     * @param inputStream
     * @return
     */
    public MinioObjectInfo upload(String fileName, String contentType, InputStream inputStream) {
        return upload(minioProperties.getBucket(), fileName, contentType,  inputStream);
    }

    /**
     * 上传文件，最全的方法，需要补全各个参数,
     *
     * @param bucket 存储桶名称
     * @param fileName 存储对象名
     * @param contentType 文件类型
     * @param inputStream 文件流
     * @return
     */
    public MinioObjectInfo upload(String bucket, String fileName, String contentType, InputStream inputStream) {
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
            String name = getPrefix() + fileName;
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(name)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            String url = minioClient.getObjectUrl(bucket, name);
            return new MinioObjectInfo()
                    .setBucket(bucket)
                    .setContentType(contentType)
                    .setName(name)
                    .setUrl(url);
        } catch (Exception e) {
            throw ExceptionManager.createByCodeAndMessage(MINIO_ERROR_CODE, "上传文件异常:" + e.getMessage());
        }
    }

    /**
     * 删除存储对象
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
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
            }
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(minioProperties.getTimePattern()));
        }
        return "";
    }




}
