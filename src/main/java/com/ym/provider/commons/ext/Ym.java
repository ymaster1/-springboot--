package com.ym.provider.commons.ext;

import java.lang.annotation.*;

/**
 * @author ymaster1
 * @date 2020/11/15 0:39
 * @description  自定义一个注解，被该注解标记的class都会被注册为bean
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ym {
}
