package com.kimzing.test.log;

import com.kimzing.log.LogAspect;
import com.kimzing.log.LogInfo;
import com.kimzing.utils.json.JsonUtil;
import com.kimzing.utils.log.LogUtil;
import org.springframework.stereotype.Component;

/**
 * 打印JSON格式日志.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/28 16:13
 */
@Component
public class JsonLogAspect extends LogAspect {

    @Override
    public void handleLogInfo(LogInfo logInfo) {
        LogUtil.info("method exec: [{}]", JsonUtil.beanToJson(logInfo));
    }
}
