package com.kimzing.dubbo.filter;

import com.kimzing.utils.exception.CustomException;
import com.kimzing.utils.exception.ServiceInfo;
import com.kimzing.utils.log.LogUtil;
import com.kimzing.utils.spring.SpringPropertyUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * ExceptionInvokerFilter
 * <p>
 * Functions:
 * <ol>
 * <li>unexpected exception will be logged in ERROR level on provider side. Unexpected exception are unchecked
 * exception not declared on the interface</li>
 * <li>Wrap the exception not introduced in API package into RuntimeException. Framework will serialize the outer exception but stringnize its cause in order to avoid of possible serialization problem on client side</li>
 * </ol>
 */
@Activate(group = CommonConstants.PROVIDER)
public class DubboExceptionFilter implements Filter, Filter.Listener {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            try {
                Throwable exception = appResponse.getException();

                // directly throw if it's checked exception
                if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                    return;
                }
                // directly throw if the exception appears in the signature
                try {
                    Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                    Class<?>[] exceptionClassses = method.getExceptionTypes();
                    for (Class<?> exceptionClass : exceptionClassses) {
                        if (exception.getClass().equals(exceptionClass)) {
                            return;
                        }
                    }
                } catch (NoSuchMethodException e) {
                    return;
                }

                String className = exception.getClass().getName();
                if (className.startsWith(SpringPropertyUtil.getValue("dubbo.provider.exception.package"))) {
                    CustomException customException = (CustomException) appResponse.getException();
                    if (customException.getMessage() == null) {
                        customException.setMessage(SpringPropertyUtil.getValueWithDefault(customException.getCode(), "异常信息未定义"));
                    }
                    ArrayList<ServiceInfo> services = customException.getServices() == null ? new ArrayList<>() : customException.getServices();
                    ServiceInfo serviceInfo = buildServiceInfo();
                    services.add(serviceInfo);
                    customException.setServices(services);
                    LogUtil.error("{}", customException);
                    return;
                }

                // for the exception not found in method's signature, print ERROR message in server's log.
                LogUtil.error("Got unchecked and undeclared exception which called by {} . service: {}, method: {}, {}: {}, {}",
                        RpcContext.getContext().getRemoteHost(),
                        invoker.getInterface().getName(),
                        invocation.getMethodName(),
                        exception.getClass().getName(),
                        exception.getMessage(),
                        exception);

                // directly throw if exception class and interface class are in the same jar file.
                String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                    return;
                }
                // directly throw if it's JDK exception
                if (className.startsWith("java.") || className.startsWith("javax.")) {
                    return;
                }

                // directly throw if it's dubbo exception
                if (exception instanceof RpcException) {
                    return;
                }

                // otherwise, wrap with RuntimeException and throw back to the client
                appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
            } catch (Throwable e) {
                LogUtil.warn("Fail to ExceptionFilter when called by {}. service: {}, method: {}, exception: {}:{}, {}",
                        RpcContext.getContext().getRemoteHost(),
                        invoker.getInterface().getName(),
                        invocation.getMethodName(),
                        e.getClass().getName(),
                        e.getMessage(),
                        e);
            }
        }
    }

    @Override
    public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
        LogUtil.error("Fail to ExceptionFilter when called by {}. service: {}, method: {}, exception: {}:{}, {}",
                RpcContext.getContext().getRemoteHost(),
                invoker.getInterface().getName(),
                invocation.getMethodName(),
                e.getClass().getName(),
                e.getMessage(),
                e);
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

