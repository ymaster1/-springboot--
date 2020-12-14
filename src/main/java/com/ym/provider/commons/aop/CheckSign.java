package com.ym.provider.commons.aop;

import java.lang.annotation.*;

/**
 * @author ymaster1
 * @date 2020/11/19 9:14
 * @description 标记该注解之后，会对接口进行签名验证
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckSign {
    /**
     * 验证接口的请求参数
     * @return
     */
    boolean check() default true;

    /**
     * 请求参数在目标方法参数列表中的位置，默认在第一个
     * @return
     */
    int reqParamIndex() default 0;
}
