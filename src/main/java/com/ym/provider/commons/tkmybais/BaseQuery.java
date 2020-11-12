package com.ym.provider.commons.tkmybais;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageHelper;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ymaster1
 * @date 2020/9/12 15:28
 * @description 基础查询应该继承该class, 提供分页参数等
 */
@Data
public class BaseQuery extends BaseObject {
    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页行数
     */
    private Integer rows;

    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date endDate;
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date startDate;

    public void queryInit() {
        PageHelper.startPage(this.page != null && this.page > 0 ? this.page : 1, this.rows != null && this.rows > 0 && this.rows <= 1000 ? this.rows : 15);
        if (this.endDate != null) {
            this.endDate = DateUtils.addDays(this.endDate, 1);
        }
    }
}
