package com.firearms.firearmcollectionjee.controller.exception;

import jakarta.ejb.EJBAccessException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for EJBAccessException.
 * Converts EJBAccessException (thrown when authorization fails) to HTTP 403 Forbidden.
 */
@Provider
public class EJBAccessExceptionMapper implements ExceptionMapper<EJBAccessException> {

    @Override
    public Response toResponse(EJBAccessException exception) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(exception.getMessage())
                .build();
    }
}
