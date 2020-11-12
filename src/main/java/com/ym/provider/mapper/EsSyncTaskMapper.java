package com.ym.provider.mapper;

import com.ym.provider.commons.tkmybais.CommonsMapper;
import com.ym.provider.entity.EsSyncTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * es数据同步任务表(EsSyncTask)表数据库访问层
 *
 * @author ymaster1
 * @since 2020-11-12 11:40:02
 */
@Mapper
public interface EsSyncTaskMapper extends CommonsMapper<EsSyncTask> {

}