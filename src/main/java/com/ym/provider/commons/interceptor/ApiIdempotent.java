package com.ym.provider.commons.interceptor;

/**
 * @author ymaster1
 * @date 2020/9/23 17:35
 * @description
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在需要保证 接口幂等性 的Controller的方法上使用此注解
 * 幂等更多的是用来防止恶意抓包，而不是针对用户，因为仅仅前端就可以通过隐藏按钮防止用户重复提交
 *
 * @author ymast
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiIdempotent {
}
