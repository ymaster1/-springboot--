package com.ym.provider.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.nacos.api.common.ResponseCode;
import com.ym.provider.commons.exception.MyError;
import com.ym.provider.commons.exception.MyException;
import com.ym.provider.commons.redis.RedisUtils;
import com.ym.provider.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ymaster1
 * @date 2020/9/24 17:55
 * @description
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    private static final String TOKEN_NAME = "token";
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 创建一个token,接口请求时带上该token
     *
     * @return
     */
    @Override
    public String createToken() {
//        生成一个随机token
        String str = RandomUtil.randomString(32);
//       将key设置到redis,设置超时，是表示该接口30s之后就必须再次申请token,不然永远无法通过校验
        redisUtils.setValueTimeout(str, str, 30L);
        return str;
    }

    @Override
    public Integer checkToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_NAME);
        // header中不存在token
        if (StringUtils.isBlank(token)) {
            return 1;
        }
//        token已经被删除（校验通过后会删除,再次提交就会报错）
        if (!redisUtils.isExist(token)) {
            return 2;
        }
//        校验通过，删除token
        boolean flag = redisUtils.delKey(token);
        log.info("flag {}",flag);
//        万一删除失败，也当没检验通过，有点扯淡
        if (!flag) {
            return 2;
        }
        return 0;
    }
}
