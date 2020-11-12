package com.ym.provider.commons.utils.excel;

import lombok.Data;

import java.util.List;

/**
 * @author hexiaohu
 * @date 2019/11/21 5:04 PM
 */
@Data
public class ImportResponse<T> {
    List<T> list;
    /**
     * 导入总记录数
     */
    private Integer totalCount;
    /**
     * 导入错误数
     */
    private Integer errorCount;
    /**
     * 成功数
     */
    private Integer successCount;
}
