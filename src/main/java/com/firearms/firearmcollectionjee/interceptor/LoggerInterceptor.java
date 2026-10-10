package com.firearms.firearmcollectionjee.interceptor;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;


import java.util.logging.Logger;

@Loggable
@Interceptor
public class LoggerInterceptor {
    private final Logger logger = Logger.getLogger(LoggerInterceptor.class.getName());

    private final SecurityContext securityContext;

    @Inject
    public LoggerInterceptor(@SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @AroundInvoke
    public Object logOperation(InvocationContext context) throws Exception {
        String operationName = context.getMethod().getName();
        Object[] parameters = context.getParameters();
        String resourceId = (parameters.length > 0) ? parameters[0].toString() : "unknown";

        String username = securityContext.getCallerPrincipal().getName();

        logger.info(String.format("User: %s, Operation: %s, Resource: %s", username, operationName, resourceId));

        return context.proceed();
    }
}