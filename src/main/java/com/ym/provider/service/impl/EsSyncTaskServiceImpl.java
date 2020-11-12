package com.ym.provider.service.impl;

import com.ym.provider.entity.EsSyncTask;
import com.ym.provider.mapper.EsSyncTaskMapper;
import com.ym.provider.service.EsSyncTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * es数据同步任务表(EsSyncTask)表服务实现类
 *
 * @author ymaster1
 * @since 2020-11-12 11:39:51
 */
@Service("esSyncTaskService")
public class EsSyncTaskServiceImpl implements EsSyncTaskService {
    @Resource
    private EsSyncTaskMapper esSyncTaskMapper;

    @Override
    public List<EsSyncTask> queryAllActive() {
        EsSyncTask task = new EsSyncTask();
        task.setStatusFlag(true);
        task.setTaskStatus(true);
        return esSyncTaskMapper.select(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(EsSyncTask esSyncTask) {
        EsSyncTask task = new EsSyncTask();
//        新增默认都直接开启
        task.setTaskStatus(true);
        if (StringUtils.isBlank(esSyncTask.getIndexName())) {
//            设置elasticsearch 索引库
            esSyncTask.setIndexName(esSyncTask.getSyncTable() + "_index");
        }
        return esSyncTaskMapper.insert(esSyncTask) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(EsSyncTask esSyncTask) {
        return esSyncTaskMapper.updateByPrimaryKeySelective(esSyncTask) > 0;
    }
}