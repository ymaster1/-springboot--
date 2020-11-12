package com.ym.provider.entity;

import com.ym.provider.commons.tkmybais.BaseLogicEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户信息表(UserInfo)表实体类
 *
 * @author ymaster1
 * @since 2020-09-11 18:03:28
 */
@Table(name = "user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserInfo extends BaseLogicEntity {

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户唯一编号
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 地址
     */
    @Column(name = "adder")
    private String adder;

    /**
     * 性别
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 国家
     */
    @Column(name = "country")
    private String country;

    /**
     * 城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 联系电话
     */
    @Column(name = "concat_phone")
    private String concatPhone;


}