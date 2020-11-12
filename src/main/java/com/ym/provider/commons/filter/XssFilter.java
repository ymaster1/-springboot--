package com.ym.provider.commons.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author ymaster1
 * @date 2020/9/23 14:49
 * @description xssFilter
 * 因为filter不是springmvc里的，是servlet里的，所以filter的执行顺序在其他3个最前面
 * filter -> interceptor -> controllerAdvice -> aop
 */
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
