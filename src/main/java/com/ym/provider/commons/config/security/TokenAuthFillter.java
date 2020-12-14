package com.ym.provider.commons.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ymaster1
 * @date 2020/11/20 11:17
 * @description
 * 实现该方法来自定义权限（去redis种获取权限，放到上下文，就可以知道当前用户的权限）
 */
public class TokenAuthFillter extends BasicAuthenticationFilter {
    public TokenAuthFillter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilterInternal(request, response, chain);
    }
}
