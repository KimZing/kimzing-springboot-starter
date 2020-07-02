package com.kimzing.web.resolver.json;

import com.kimzing.utils.json.JsonUtil;
import com.kimzing.utils.log.LogUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 将Url中的Json格式的查询参数转换为Java Object
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/28 11:37
 */
public class JsonParamResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断是否是需要解析的参数类型
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JsonParam.class);
    }

    /**
     * 解析方法
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String[]> parameterMap = request.getParameterMap();

        String jsonParam = parameterMap.keySet().stream().findFirst().orElse(null);
        Class<?> parameterType = methodParameter.getParameterType();
        JsonParam parameterAnnotation = methodParameter.getParameterAnnotation(JsonParam.class);

        if (JsonUtil.isValid(jsonParam)) {
            return JsonUtil.jsonToBean(jsonParam, parameterType);
        }

        if (parameterAnnotation.required()) {
            throw new IllegalArgumentException(parameterType.getSimpleName());
        }

        LogUtil.warn("param [{}] is not json format", jsonParam);
        return null;
    }
}