package com.ym.provider.commons.interceptor;

import com.ym.provider.commons.utils.ApplicationContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ymaster1
 * @date 2020/9/23 15:16
 * @description
 * 继承WebMvcConfigurationSupport类是会导致自动配置失效的.
 * 自动配置的静态资源路径（classpath:/META/resources/，
 * classpath:/resources/，classpath:/static/，classpath:/public/）不生效(参考WebMvcAutoConfiguration)
 * 一般通过实现WebMvcConfigurer添加拦截器
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    /**
     * 添加拦截器
     * addPathPatterns用来设置拦截路径，
     * excludePathPatterns 用来设置白名单，也就是不需要触发这个拦截器的路径
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        这里必须把ApiIdempotentInterceptor注入去，不然它里面不能注入其他service
        registry.addInterceptor(ApplicationContextHolder.getBean(ApiIdempotentInterceptor.class))
//                拦截所有请求
                .addPathPatterns("/**");
    }
}
