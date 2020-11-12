package com.ym.provider.commons.interceptor;

import com.ym.provider.commons.exception.MyException;
import com.ym.provider.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author ymaster1
 * @date 2020/9/23 14:46
 * @description 接口幂等拦截
 * 只有经过 dispatcherservlet 的请求，才会走拦截器chain，我们自定义的的servlet 请求是不会被拦截的
 * 我们不能通过修改拦截器修改request内容
 * 也可以通过继承HandlerInterceptorAdapter（实现了HandlerInterceptor），都一样
 */
@Slf4j
@Configuration
public class ApiIdempotentInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    /**
     * controller 方法执行前执行，当return TRUE 时进入下一个 拦截器 或直接进入 Controller 方法
     *
     * @param request
     * @param response
     * @param handler  AbstractHandlerMapping(类中)
     *                 Object handler = getHandlerInternal(HttpServletRequest request)
     *                 Handler一般都是我们写的带有@Controller的注解的类，
     *                 它是对Controller的Bean本身和请求Method的包装。
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入拦截器，此时还未到controller");
//  HandlerMethod 是一个包含了handler的Bean本身和请求方法的对象！也就是说，
//  所谓的handler在这里，是指包含了我们请求的Controller类和Method方法的对象
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
//        获取其方法签名
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        ApiIdempotent methodAnnotation = method.getAnnotation(ApiIdempotent.class);
//        如果带有该注解，就校验
        if (methodAnnotation != null) {
            Integer check = check(request);
            switch (check){
                case 1:
                    throw new MyException("非法请求");
                case 2:
                    throw new MyException("请不要重复提交");
                case 0:
                    return true;
                default:
                    return false;
            }
        }
        return true;
    }

    /**
     * @param request
     */
    private Integer check(HttpServletRequest request) {
        log.info("做校验了");
        return tokenService.checkToken(request);
    }
}
