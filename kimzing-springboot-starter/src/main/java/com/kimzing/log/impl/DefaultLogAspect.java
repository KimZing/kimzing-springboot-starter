package com.kimzing.log.impl;

import com.kimzing.log.LogAspect;
import com.kimzing.log.LogInfo;
import com.kimzing.utils.log.LogUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认日志信息处理类.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/26 11:57
 */
public class DefaultLogAspect extends LogAspect {

    /**
     * 对日志信息的处理
     *
     * @param logInfo
     */
    @Override
    public void handleLogInfo(LogInfo logInfo) {
        // 为了防止日志打印出错，将内部错误捕获，防止影响主业务
        try {
            LogUtil.info("[{}]", logInfo);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.warn("切面日志打印异常: [{}]", e.getMessage());
        }
    }
}
