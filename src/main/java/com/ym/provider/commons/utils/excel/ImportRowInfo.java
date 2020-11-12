package com.ym.provider.commons.utils.excel;

import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 导入行信息类
 * @author hexiaohu
 * @date 2019/11/21 4:39 PM
 */
@Data
@JsonIgnoreProperties({"cellStyleMap"})
public class ImportRowInfo extends BaseRowModel implements Serializable {

    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 导入状态
     */
    private boolean importStatus;
}
