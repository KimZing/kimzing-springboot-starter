package com.kimzing.web.resolver.json;

import com.kimzing.utils.exception.ExceptionManager;
import com.kimzing.utils.json.JsonUtil;
import com.kimzing.utils.log.LogUtil;
import com.kimzing.utils.string.StringUtil;
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

        // 获取对应的参数解析名称，如果JsonParam.name()不为空则取其值，如果为空则取方法参数变量名
        String paramName = methodParameter.getParameter().getName();
        JsonParam parameterAnnotation = methodParameter.getParameterAnnotation(JsonParam.class);
        if (!StringUtil.isBlank(parameterAnnotation.name())) {
            paramName = parameterAnnotation.name();
        }

        String paramValue = null;
        // 获取请求值
        if (parameterMap != null && parameterMap.get(paramName) != null && parameterMap.size() > 0) {
            paramValue = parameterMap.get(paramName)[0];
        }
        // 获取参数类型
        Class<?> parameterType = methodParameter.getParameterType();

        if (!JsonUtil.isValid(paramValue)) {
            if (parameterAnnotation.required()) {
                throw ExceptionManager.createByCodeAndMessage("PARAM_ERROR",
                        String.format("param %s is required", parameterType.getSimpleName()));
            }
            LogUtil.warn("param [{}] is not json format", paramValue);
            return null;
        }

        return JsonUtil.jsonToBean(paramValue, parameterType, parameterAnnotation.pattern());
    }
}