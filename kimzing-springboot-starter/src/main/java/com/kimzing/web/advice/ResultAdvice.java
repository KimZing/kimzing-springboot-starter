package com.kimzing.web.advice;

import com.kimzing.utils.exception.CustomException;
import com.kimzing.utils.json.JsonUtil;
import com.kimzing.utils.result.ApiResult;
import com.kimzing.utils.spring.SpringPropertyUtil;
import com.kimzing.utils.string.StringUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一结果处理器.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/7 19:10
 */
@RestControllerAdvice
public class ResultAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        String packages = SpringPropertyUtil.getValue("kimzing.web.result.package");
        Assert.isTrue(!StringUtil.isBlank(packages), "结果包装的包路径不能为空");

        String[] strings = StringUtils.commaDelimitedListToStringArray(packages);
        for (String s : strings) {
            if (returnType.getDeclaringClass().getName().startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 如果已经做了包装，则不处理
        if (body instanceof ApiResult) {
            return body;
        }
        int status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
        // 如果状态码是200，则包装为成功返回体
        if (status == HttpStatus.OK.value()) {
            return ApiResult.success(body);
        }
        // 状态码不是200，如果是捕捉到的异常，则对异常信息进行读取并包装成errorResult
        if (body instanceof CustomException) {
            CustomException customException = (CustomException) body;
            return ApiResult.error(customException.getCode(), customException.getMessage());
        }
        // 其他返回体则直接返回对应的字符串信息
        return ApiResult.error("UNCATCHED_ERROR_CODE", JsonUtil.beanToJson(body));
    }
}
