package com.kimzing.web.log;

import com.kimzing.utils.date.DateUtil;
import com.kimzing.utils.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志切面基础类.
 * <p>
 * 仅当配置kimzing.logging.enabled=true时生效
 * </p>
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/24 15:04
 */
@Slf4j
@Aspect
public class WebRequestLogAspect {

    @Value("${yyyy-MM-dd HH:mm:ss:SSS}")
    private String timePattern;

    @Pointcut("(@within(org.springframework.stereotype.Controller) || " +
            "@within(org.springframework.web.bind.annotation.RestController)) && " +
            "execution(* com.kimzing..*.*(..))")
    public void logPointCut() {
    }

    /**
     * 对方法进行环绕处理
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimeOfMethod = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTimeOfMethod = System.currentTimeMillis();

        WebLogInfo.WebLogInfoBuilder builder = WebLogInfo.builder()
                .result(result)
                .startTime(DateUtil.formatUnixTimeToLocalDateTime(startTimeOfMethod, timePattern))
                .endTime(DateUtil.formatUnixTimeToLocalDateTime(endTimeOfMethod, timePattern))
                .elapsedTimeInMilliseconds(endTimeOfMethod - startTimeOfMethod);
        setLogAttributes(joinPoint, builder);

        handleWebLogInfo(builder.build());
        return result;
    }

    /**
     * 方法出错时的处理
     *
     * @param joinPoint
     * @param throwable
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "throwable")
    public void throwExcetion(JoinPoint joinPoint, Throwable throwable) {
        WebLogInfo.WebLogInfoBuilder builder = WebLogInfo.builder();
        setLogAttributes(joinPoint, builder);
        builder.throwable(throwable);
        handleWebLogInfo(builder.build());
    }

    /**
     * 对日志信息的处理
     *
     * @param logInfo
     */
    public void handleWebLogInfo(WebLogInfo logInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n================  Request Start  ================\n");
        sb.append(JsonUtil.beanToJson(logInfo, timePattern));
        sb.append("\n================  Request End  ================");
        log.info(sb.toString());
    }

    ;

    /**
     * 对日志信息进行解析并添加进日志信息中
     *
     * @param joinPoint
     * @param builder
     */
    private void setLogAttributes(JoinPoint joinPoint, WebLogInfo.WebLogInfoBuilder builder) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String url = (requestAttributes == null) ? null :
                ((ServletRequestAttributes) requestAttributes).getRequest().getRequestURI();
        builder.className(getClassName(joinPoint))
                .methodName(getMethodName(signature))
                .url(url)
                .params(getParams(joinPoint));
    }

    /**
     * 获取方法参数
     *
     * @param joinPoint
     * @return
     */
    private Map<String, Object> getParams(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Map<String, Object> argsMap = new HashMap<>();

        for (Object arg : args) {
            if (arg != null) {
                argsMap.put(arg.getClass().getSimpleName(), arg);
            }
        }
        return argsMap;
    }

    /**
     * 获取方法名
     *
     * @param signature
     * @return
     */
    private String getMethodName(MethodSignature signature) {
        return signature.getName();
    }

    /**
     * 获取类名
     *
     * @param joinPoint
     * @return
     */
    private String getClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName();
    }

}
