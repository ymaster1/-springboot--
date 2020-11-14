package com.ym.provider.service.impl;

import com.ym.provider.entity.TimingTask;
import com.ym.provider.mapper.TimingTaskMapper;
import com.ym.provider.service.TimingTaskService;
import com.ym.provider.task.table.CronTaskRegistrar;
import com.ym.provider.task.table.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private CronTaskRegistrar registrar;

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
     * 新增任务，并默认启动
     *
     * @param timingTask 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(TimingTask timingTask) {
//        强制设置为启动
        timingTask.setTaskStatus(true);
        int insert = this.timingTaskmapper.insert(timingTask);
        if (insert > 0) {
//            新增成功之后直接启动任务
            registrar.addCronTask(new Task(timingTask.getTaskName(), timingTask.getServiceName(),
                    timingTask.getMethodName()), timingTask.getTaskCron());
            return true;
        }
        return false;
    }

    /**
     * 修改任务，需要先删除任务，再添加任务
     *
     * @param timingTask 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(TimingTask timingTask) {
        int update = this.timingTaskmapper.updateByPrimaryKeySelective(timingTask);
        return true;
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