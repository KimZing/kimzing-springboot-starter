package com.kimzing.utils.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据实体公共属性.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/28 13:10
 */
@Data
public abstract class AbstractPO {

    protected String creater;

    protected String modifier;

    protected LocalDateTime createTime;

    protected LocalDateTime modifyTime;

}
