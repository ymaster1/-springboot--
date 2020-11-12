package com.ym.provider.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.ym.provider.commons.utils.excel.ImportRowInfo;
import lombok.Data;

/**
 * @author ymaster1
 * @date 2020/10/26 20:23
 * @description
 */
@Data
public class UserImportVo extends ImportRowInfo {
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名",index = 0)
    private String userName;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址",index = 1)
    private String adder;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别",index = 2)
    private Integer sex;


    /**
     * 城市
     */
    @ExcelProperty(value = "城市",index = 3)
    private String city;

}
