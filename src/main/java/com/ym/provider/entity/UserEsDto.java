package com.ym.provider.entity;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author ymaster1
 * @date 2020/11/5 17:22
 * @description
 */
@Data
public class UserEsDto {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户唯一编号
     */
    private String userId;

    /**
     * 地址
     */
    private String adder;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 国家
     */
    private String country;

    /**
     * 城市
     */
    private String city;

    /**
     * 联系电话
     */
    private String concatPhone;
}
