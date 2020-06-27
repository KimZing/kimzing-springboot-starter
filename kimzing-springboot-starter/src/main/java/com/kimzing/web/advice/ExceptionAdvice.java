package com.kimzing.web.advice;

import com.kimzing.utils.exception.BusinessException;
import com.kimzing.utils.result.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * 异常捕捉.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/6 16:53
 */
@RestControllerAdvice
public class ExceptionAdvice {

    //TODO 标记人:kimzing,时间:2019/12/26 23:34,备注: 添加各种常见异常，适用方可以直接覆盖

    /**
     * 服务端自定义异常处理
     * @param businessException
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult handlerBusinessException(BusinessException businessException) {
        return ApiResult.error(businessException.getMessage());
    }

    /**
     * 校验异常处理
     * @param validationException
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult handlerValidationException(ValidationException validationException) {
        // 如果是ConstraintViolationException，则可能出现多个异常信息
        if (validationException instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException =
                    (ConstraintViolationException) validationException;

            String[] codes = constraintViolationException.getConstraintViolations().stream()
                    .map(c -> c.getMessage()).collect(Collectors.toList()).toArray(new String[]{});
            return ApiResult.error(codes);
        }
        return ApiResult.error(validationException.getMessage());
    }


    /**
     * 意料之外的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult handlerUncatchException(Exception e) {
        return ApiResult.error("-1", e.getMessage());
    }

}
