package com.unisguard.webapi.config.filter;

import com.unisguard.webapi.config.wrapper.MultiReadHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author zemel
 * @date 2021/12/26 17:17
 */
public class MultiReadFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getContentType() == null) {
            chain.doFilter(request, response);
            return;
        }
        if (request.getContentType().contains("multipart/form-data")) {
            chain.doFilter(request, response);
            return;
        }
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(new MultiReadHttpServletRequestWrapper((HttpServletRequest) request), response);
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
}
