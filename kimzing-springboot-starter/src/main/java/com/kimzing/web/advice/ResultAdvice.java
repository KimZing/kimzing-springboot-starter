package com.kimzing.web.advice;

import com.kimzing.utils.exception.CustomException;
import com.kimzing.utils.result.ApiResult;
import com.kimzing.utils.spring.SpringPropertyUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一结果处理器.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/7 19:10
 */
@ControllerAdvice
public class ResultAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getDeclaringClass().getName().startsWith(SpringPropertyUtil.getValue("kimzing.web.result.package"));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        int status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
        if (status == HttpStatus.OK.value()) {
            return ApiResult.success(body);
        }
        CustomException customException = (CustomException) body;
        return ApiResult.error(customException.getCode(), customException.getMessage());
    }
}
