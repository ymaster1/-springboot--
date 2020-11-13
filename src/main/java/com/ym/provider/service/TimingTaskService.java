package com.ym.provider.service;

import com.ym.provider.entity.TimingTask;

import java.util.List;

/**
 * 定时任务表(TimingTask)表服务接口
 *
 * @author ymaster1
 * @since 2020-11-13 15:56:22
 */
public interface TimingTaskService {
    /**
     * 数据库定时任务测试
     */
    void testTask();

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TimingTask queryById(Long id);

    /**
     * 批量查找
     *
     * @param timingTask 实例对象
     * @return list
     */
    List<TimingTask> queryList(TimingTask timingTask);

    /**
     * 新增数据
     *
     * @param timingTask 实例对象
     * @return 是否成功
     */
    boolean insert(TimingTask timingTask);

    /**
     * 修改数据
     *
     * @param timingTask 实例对象
     * @return 是否成功
     */
    boolean update(TimingTask timingTask);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}