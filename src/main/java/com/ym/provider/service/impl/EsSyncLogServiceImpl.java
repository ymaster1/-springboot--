package com.ym.provider.service.impl;

import com.ym.provider.dao.EsSyncLogDao;
import com.ym.provider.entity.EsSyncLog;
import com.ym.provider.service.EsSyncLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * es数据同步任务表日志表(EsSyncLog)表服务实现类
 *
 * @author ymaster1
 * @since 2020-11-12 14:56:13
 */
@Service("esSyncLogService")
public class EsSyncLogServiceImpl implements EsSyncLogService {
    @Resource
    private EsSyncLogDao esSyncLogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EsSyncLog queryById(Long id) {
        return this.esSyncLogDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EsSyncLog> queryAllByLimit(int offset, int limit) {
        return this.esSyncLogDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param esSyncLog 实例对象
     * @return 实例对象
     */
    @Override
    public EsSyncLog insert(EsSyncLog esSyncLog) {
        this.esSyncLogDao.insert(esSyncLog);
        return esSyncLog;
    }

    /**
     * 修改数据
     *
     * @param esSyncLog 实例对象
     * @return 实例对象
     */
    @Override
    public EsSyncLog update(EsSyncLog esSyncLog) {
        this.esSyncLogDao.update(esSyncLog);
        return this.queryById(esSyncLog.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.esSyncLogDao.deleteById(id) > 0;
    }
}