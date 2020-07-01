package com.kimzing.utils.page;

import lombok.Data;

/**
 * 分页查询对象.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/2 00:38
 */
@Data
public class PageParam {

    /**
     * 当前页数
     */
    private Integer pageNum;

    /**
     * 分页大小
     */
    private Integer pageSize;

}
