package com.ym.provider.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ymaster1
 * @date 2020/9/24 17:54
 * @description
 */
public interface TokenService {
    /**
     * 创建 token
     *
     * @return
     */
    String createToken();

    /**
     * 检验token
     *
     * @return
     */
    Integer checkToken(HttpServletRequest request);
}
