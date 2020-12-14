package com.ym.provider.commons.tkmybais;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @author ymaster1
 * @date 2020/9/12 15:29
 * @description
 */
public class BaseObject implements Serializable {

    /**
     * bean转换
     *
     * @param clazz
     * @param
     * @return
     */
    public void convert(Class clazz) {
        BeanUtils.copyProperties(clazz, this);
    }

    /**
     * bean转换
     * @param clazz
     */
    public void reconvert(Class clazz) {
        BeanUtils.copyProperties(this, clazz);
    }
}
