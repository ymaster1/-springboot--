package com.ym.provider.mapper;

import com.ym.provider.commons.tkmybais.CommonsMapper;
import com.ym.provider.entity.TimingTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务表(TimingTask)表数据库访问层
 *
 * @author ymaster1
 * @since 2020-11-13 15:33:36
 */
@Mapper
public interface TimingTaskMapper extends CommonsMapper<TimingTask> {

}