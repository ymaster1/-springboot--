package com.ym.provider.commons.config.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author ymaster1
 * @date 2020/11/20 10:46
 * @description jwt token工具
 * 实现该接口可以在退出的时候做一些事情，比如删除token(登录的时候存在redis里面的<username,权限列表>)
 * token都是放在header里面的
 */
@Component
public class TokenUtil implements LogoutHandler {
    /**
     * 根据用户名生成token
     *
     * @param name
     * @return
     */
    public String createToken(String name) {
        String token = Jwts.builder().setSubject(name)
//                设置token过期时间
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.ES512, "123").compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    /**
     * 根据token解析得到用户名
     *
     * @param token
     * @return
     */
    public String parse(String token) {
        String name = Jwts.parser().setSigningKey("123").parseClaimsJws(token).getBody().getSubject();
        return name;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

    }
}
