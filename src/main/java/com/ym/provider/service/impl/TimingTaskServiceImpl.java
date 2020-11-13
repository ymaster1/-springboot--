package com.ym.provider.service.impl;

import com.ym.provider.entity.TimingTask;
import com.ym.provider.mapper.TimingTaskMapper;
import com.ym.provider.service.TimingTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务表(TimingTask)表服务实现类
 *
 * @author ymaster1
 * @since 2020-11-13 16:12:57
 */
@Service("timingTaskService")
@Slf4j
public class TimingTaskServiceImpl implements TimingTaskService {
    @Resource
    private TimingTaskMapper timingTaskmapper;

    @Override
    public void testTask() {
        log.info("[{}]:定时任务测试", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                "HH:mm:ss")));
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TimingTask queryById(Long id) {
        return null;
    }

    /**
     * 查找多条
     *
     * @param timingTask 实例对象
     * @return 实例对象列表
     */
    @Override
    public List<TimingTask> queryList(TimingTask timingTask) {
        return timingTaskmapper.select(timingTask);
    }

    /**
     * 新增数据
     *
     * @param timingTask 实例对象
     * @return 实例对象
     */
    @Override
    public boolean insert(TimingTask timingTask) {
        return this.timingTaskmapper.insert(timingTask) > 0;
    }

    /**
     * 修改数据
     *
     * @param timingTask 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(TimingTask timingTask) {

        return this.timingTaskmapper.updateByPrimaryKeySelective(timingTask) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        TimingTask entity = new TimingTask();
        entity.setStatusFlag(false);
        return this.timingTaskmapper.updateByPrimaryKeySelective(entity) > 0;
    }
}