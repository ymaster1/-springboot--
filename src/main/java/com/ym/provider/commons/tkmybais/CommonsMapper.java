package com.ym.provider.commons.tkmybais;

import tk.mybatis.mapper.common.*;

/**
 * @author ymaster1
 * @date 2020/9/10 19:30
 * @description  一般业务Mapper继承它,特别注意，该接口不能被扫描到，否则会出错
 */
public interface CommonsMapper<T>  extends
        BaseMapper<T>,
        MySqlMapper<T>,
        IdsMapper<T>,
        ExampleMapper<T>,
        RowBoundsMapper<T> {
}
