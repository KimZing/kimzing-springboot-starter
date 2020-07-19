package com.kimzing.log;

import com.kimzing.utils.date.DateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 日志切面基础类.
 * <p>
 * 仅当配置kimzing.logging.enabled=true时生效
 * </p>
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/24 15:04
 */
@Aspect
public abstract class LogAspect {

    @Value("${base.log.time-pattern:yyyy-MM-dd HH:mm:ss:SSS}")
    private String timePattern;

    @Pointcut("@annotation(com.kimzing.log.LogKim)")
    public void logPointCut() {}

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

        LogInfo.LogInfoBuilder builder = LogInfo.builder()
                .result(result)
                .startTime(DateUtil.formatUnixTimeToLocalDateTime(startTimeOfMethod, timePattern))
                .endTime(DateUtil.formatUnixTimeToLocalDateTime(endTimeOfMethod, timePattern))
                .elapsedTimeInMilliseconds(endTimeOfMethod - startTimeOfMethod);
        setLogAttributes(joinPoint, builder);

        handleLogInfo(builder.build());
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
        LogInfo.LogInfoBuilder builder = LogInfo.builder();
        setLogAttributes(joinPoint, builder);
        builder.throwable(throwable);
        handleLogInfo(builder.build());
    }

    /**
     * 对日志信息的处理
     *
     * @param logInfo
     */
    public abstract void handleLogInfo(LogInfo logInfo);

    /**
     * 对日志信息进行解析并添加进日志信息中
     *
     * @param joinPoint
     * @param builder
     */
    private void setLogAttributes(JoinPoint joinPoint, LogInfo.LogInfoBuilder builder) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        builder.className(getClassName(joinPoint))
                .methodName(getMethodName(signature))
                .desc(getMethodDesc(signature))
                .params(getParams(joinPoint));
    }

    /**
     * 获取方法参数
     *
     * @param joinPoint
     * @return
     */
    private Map<String, Object> getParams(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();

        // 获取有哪些位置的参数添加了@IgnoreLogParam注解
        List<Integer> ignoreParamIndex = new ArrayList<>();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            if (parameterAnnotations[i] != null && parameterAnnotations[i].length > 0) {
                for (int j = 0; j < parameterAnnotations[i].length; j++) {
                    if (parameterAnnotations[i][j].annotationType() == LogIgnore.class) {
                        ignoreParamIndex.add(i);
                    }
                }
            }
        }

        Object[] args = joinPoint.getArgs();
        Map<String, Object> argsMap = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            if (!ignoreParamIndex.contains(i) && args[i] != null) {
                argsMap.put(args[i].getClass().getSimpleName(), args[i]);
            }
        }

        return argsMap;
    }

    /**
     * 获取方法描述
     *
     * @param signature
     * @return
     */
    private String getMethodDesc(MethodSignature signature) {
        LogKim annotation = signature.getMethod().getAnnotation(LogKim.class);
        return annotation.desc();
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
