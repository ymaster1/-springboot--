package com.ym.provider.service.impl;

import com.ym.provider.mapper.EsSyncLogMapper;
import com.ym.provider.service.EsSyncLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * es数据同步任务表日志表(EsSyncLog)表服务实现类
 *
 * @author ymaster1
 * @since 2020-11-12 14:56:13
 */
@Service("esSyncLogService")
public class EsSyncLogServiceImpl implements EsSyncLogService {
    @Resource
    private EsSyncLogMapper esSyncLogDao;


}