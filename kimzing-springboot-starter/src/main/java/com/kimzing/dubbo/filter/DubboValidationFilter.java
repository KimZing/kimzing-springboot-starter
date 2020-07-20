/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kimzing.dubbo.filter;

import com.kimzing.utils.exception.CustomException;
import com.kimzing.utils.exception.ExceptionManager;
import com.kimzing.utils.exception.ServiceInfo;
import com.kimzing.utils.log.LogUtil;
import com.kimzing.utils.spring.SpringPropertyUtil;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ConfigUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.validation.Validation;
import org.apache.dubbo.validation.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Set;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;
import static org.apache.dubbo.common.constants.FilterConstants.VALIDATION_KEY;

/**
 * ValidationFilter invoke the validation by finding the right {@link Validator} instance based on the
 * configured <b>validation</b> attribute value of invoker url before the actual method invocation.
 *
 * <pre>
 *     e.g. &lt;dubbo:method name="save" validation="jvalidation" /&gt;
 *     In the above configuration a validation has been configured of type jvalidation. On invocation of method <b>save</b>
 *     dubbo will invoke {@link org.apache.dubbo.validation.support.jvalidation.JValidator}
 * </pre>
 *
 * To add a new type of validation
 * <pre>
 *     e.g. &lt;dubbo:method name="save" validation="special" /&gt;
 *     where "special" is representing a validator for special character.
 * </pre>
 *
 * developer needs to do
 * <br/>
 * 1)Implement a SpecialValidation.java class (package name xxx.yyy.zzz) either by implementing {@link Validation} or extending {@link org.apache.dubbo.validation.support.AbstractValidation} <br/>
 * 2)Implement a SpecialValidator.java class (package name xxx.yyy.zzz) <br/>
 * 3)Add an entry <b>special</b>=<b>xxx.yyy.zzz.SpecialValidation</b> under <b>META-INF folders org.apache.dubbo.validation.Validation file</b>.
 *
 * 一定要注意order顺序问题
 *
 * @see Validation
 * @see Validator
 * @see Filter
 * @see org.apache.dubbo.validation.support.AbstractValidation
 */
@Activate(group = {CONSUMER, PROVIDER}, value = VALIDATION_KEY, order = 1)
public class DubboValidationFilter implements Filter {

    private Validation validation;

    /**
     * Sets the validation instance for ValidationFilter
     * @param validation Validation instance injected by dubbo framework based on "validation" attribute value.
     */
    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    /**
     * Perform the validation of before invoking the actual method based on <b>validation</b> attribute value.
     * @param invoker    service
     * @param invocation invocation.
     * @return Method invocation result
     * @throws RpcException Throws RpcException if  validation failed or any other runtime exception occurred.
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (validation != null && !invocation.getMethodName().startsWith("$")
                && ConfigUtils.isNotEmpty(invoker.getUrl().getMethodParameter(invocation.getMethodName(), VALIDATION_KEY))) {
            try {
                Validator validator = validation.getValidator(invoker.getUrl());
                if (validator != null) {
                    validator.validate(invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
                }
            } catch (RpcException e) {
                throw e;
            } catch (ConstraintViolationException e) {
                Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
                if (constraintViolations.size() > 0) {
                    // 不用关心所有的校验失败情况，关心一个就够了
                    String message = constraintViolations.iterator().next().getMessage();
                    CustomException customException = ExceptionManager.createByCodeAndMessage(message,
                            SpringPropertyUtil.getValueWithDefault(message, "未设置校验提示信息"));
                    ArrayList<ServiceInfo> services = customException.getServices() == null ? new ArrayList<>() : customException.getServices();
                    ServiceInfo serviceInfo = buildServiceInfo();
                    services.add(serviceInfo);
                    customException.setServices(services);
                    LogUtil.error("{}", customException);
                    return AsyncRpcResult.newDefaultAsyncResult(customException, invocation);
                }

                // only use exception's message to avoid potential serialization issue
                return AsyncRpcResult.newDefaultAsyncResult(new ValidationException(e.getMessage()), invocation);
            } catch (Throwable t) {
                return AsyncRpcResult.newDefaultAsyncResult(t, invocation);
            }
        }
        return invoker.invoke(invocation);
    }

    private static ServiceInfo buildServiceInfo() {
        RpcContext context = RpcContext.getContext();
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setAddress(context.getLocalAddressString());
        serviceInfo.setAppName(context.getUrl().getParameter("application"));
        serviceInfo.setInterName(context.getUrl().getParameter("interface"));
        serviceInfo.setMethodName(context.getMethodName());
        return serviceInfo;
    }

}
