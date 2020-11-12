package com.ym.provider.service;

import com.ym.provider.entity.EsSyncLog;

import java.util.List;

/**
 * es数据同步任务表日志表(EsSyncLog)表服务接口
 *
 * @author ymaster1
 * @since 2020-11-12 14:56:12
 */
public interface EsSyncLogService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EsSyncLog queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EsSyncLog> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param esSyncLog 实例对象
     * @return 实例对象
     */
    EsSyncLog insert(EsSyncLog esSyncLog);

    /**
     * 修改数据
     *
     * @param esSyncLog 实例对象
     * @return 实例对象
     */
    EsSyncLog update(EsSyncLog esSyncLog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}