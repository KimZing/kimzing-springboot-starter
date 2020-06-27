package com.kimzing.utils.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 成功返回体.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/4 17:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public final class SuccessApiResult<T> extends ApiResult {

    public SuccessApiResult() {
        this(null);
    }

    public SuccessApiResult(T data) {
        this.ts = System.currentTimeMillis();
        this.code = "0";
        this.message = "SUCCESS";
        this.data = data;
    }

    private T data;

}
