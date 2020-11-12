package com.ym.provider.service;

import com.ym.provider.entity.EsSyncTask;

import java.util.List;

/**
 * es数据同步任务表(EsSyncTask)表服务接口
 *
 * @author ymaster1
 * @since 2020-11-12 11:39:50
 */
public interface EsSyncTaskService {

    /**
     * 查询所有开启的定时任务
     *
     * @return
     */
    List<EsSyncTask> queryAllActive();

    /**
     * 新增数据
     *
     * @param esSyncTask 实例对象
     * @return 实例对象
     */
    boolean insert(EsSyncTask esSyncTask);

    /**
     * 修改任务属性
     *
     * @param esSyncTask 0 停用  1 启用
     * @return 实例对象
     */
    boolean updateStatus(EsSyncTask esSyncTask);


}