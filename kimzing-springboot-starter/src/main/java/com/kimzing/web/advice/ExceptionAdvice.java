package com.kimzing.web.advice;

import com.kimzing.utils.exception.CustomException;
import com.kimzing.utils.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * 异常捕捉.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/6 16:53
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    //TODO 标记人:kimzing,时间:2019/12/26 23:34,备注: 添加各种常见异常，适用方可以直接覆盖

    /**
     * 服务端自定义异常处理
     * @param customException
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomException handlerBusinessException(CustomException customException) {
        log.error("{}", customException);
        return customException;
    }

    /**
     * 校验异常处理
     * @param validationException
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomException handlerValidationException(ValidationException validationException) {
        log.error("{}", validationException);
        // 如果是ConstraintViolationException，则可能出现多个异常信息
        if (validationException instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException =
                    (ConstraintViolationException) validationException;

            String message = constraintViolationException.getConstraintViolations().stream()
                    .map(c -> c.getMessage()).findFirst().get();
            return ExceptionManager.createByCodeAndMessage("VALIDATION", message);
        }
        return ExceptionManager.createByCodeAndMessage("VALIDATION", validationException.getMessage());
    }


    /**
     * 意料之外的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomException handlerUncatchException(Exception e) {
        log.error("{}", e);
        return ExceptionManager.createByCodeAndMessage("SYSTEM", e.getMessage());
    }

}
