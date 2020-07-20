package com.kimzing.minio;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * mini对象信息.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/20 03:21
 */
@Data
@Accessors(chain = true)
public class MinioObjectInfo {

    /**
     * 存储桶名称
     */
    private String bucket;

    /**
     * 存储路径
     */
    private String path;

    /**
     * 保存的文件名
     */
    private String name;

    /**
     * 数据类型
     */
    private String contentType;

    /**
     * url
     */
    private String url;



}
