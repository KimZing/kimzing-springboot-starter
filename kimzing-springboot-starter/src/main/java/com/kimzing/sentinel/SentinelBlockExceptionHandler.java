package com.kimzing.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.kimzing.utils.exception.ExceptionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SentinelBlockExceptionHandler implements BlockExceptionHandler {

    private String code;

    private String message;

    public SentinelBlockExceptionHandler(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        throw ExceptionManager.createByCodeAndMessage(code, message);
    }

}