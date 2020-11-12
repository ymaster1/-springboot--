package com.ym.provider.commons.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ymaster1
 * @date 2020/9/23 17:39
 * @description
 * 对方法耗时做统计
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeTotal {
}
