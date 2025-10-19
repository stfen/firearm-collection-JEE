package com.firearms.firearmcollectionjee.controller.filter;

import com.firearms.firearmcollectionjee.controller.servlet.exception.HttpRequestException;
import com.firearms.firearmcollectionjee.controller.servlet.ApiServlet;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {
        ApiServlet.Paths.API + "/*"
})
public class ExceptionFilter extends HttpFilter implements Filter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(request, response, chain);
        } catch (HttpRequestException ex) {
            response.sendError(ex.getResponseCode());
        }
    }

}
