package com.kimzing.utils.page;

import lombok.Data;

import java.util.List;

/**
 * 分页类.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/5 15:33
 */
@Data
public class PageResult<T> {

    public PageResult(Long total, Integer pageNum, Integer pageSize, List<T> data) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = data;
    }

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 分页数据
     */
    private List<T> data;

}
