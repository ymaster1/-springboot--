package com.ym.provider.mapper;

import com.ym.provider.commons.tkmybais.CommonsMapper;
import com.ym.provider.entity.EsSyncLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * es数据同步任务表日志表(EsSyncLog)表数据库访问层
 *
 * @author ymaster1
 * @since 2020-11-12 14:56:13
 */
@Mapper
public interface EsSyncLogMapper extends CommonsMapper<EsSyncLog> {

}