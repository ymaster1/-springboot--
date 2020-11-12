package com.ym.provider.commons.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ymaster1
 * @date 2020/10/27 18:15
 * @description 导入数据时需要使用该类监听每一行数据
 */
public class ExcelListener<T> extends AnalysisEventListener<T> {
    /**自定义用于暂时存储data。
     * 可以通过实例获取该值
     */
    private List<T> data = new ArrayList<>();

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(T object, AnalysisContext context) {
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        data.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    public List<T> getDatas() {
        return data;
    }
}
