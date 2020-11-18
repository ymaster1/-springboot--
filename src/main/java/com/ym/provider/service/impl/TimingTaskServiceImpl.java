package com.ym.provider.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ym.provider.entity.TimingTask;
import com.ym.provider.entity.UserInfo;
import com.ym.provider.mapper.TimingTaskMapper;
import com.ym.provider.service.TimingTaskService;
import com.ym.provider.task.table.CronTaskRegistrar;
import com.ym.provider.task.table.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.CronTask;
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
        TimingTask task = new TimingTask();
        task.setId(id);
        task.setStatusFlag(true);
        return timingTaskmapper.selectOne(task);
    }

    @Override
    public PageInfo<TimingTask> queryPage(TimingTask timingTask) {
        PageHelper.startPage(1, 10);
        List<TimingTask> page = timingTaskmapper.select(timingTask);
        PageInfo<TimingTask> pageInfo = new PageInfo<>(page);
        return pageInfo;
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
     * @param task 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(TimingTask task) {
        TimingTask old = queryById(task.getId());
        if (old == null) {
            return false;
        }
        int update = this.timingTaskmapper.updateByPrimaryKeySelective(task);
        if (update > 0) {
            Task oldTask = new Task(old.getTaskName(), old.getServiceName(), old.getMethodName());
//            如果原状态时开启
            if (old.getTaskStatus()) {
//                off 先移除
                registrar.removeCronTask(oldTask);
//                update 如果更新过后的状态还是开启，说明时修改了其他属性，再添加
                if (task.getTaskStatus()) {
                    registrar.addCronTask(new CronTask(new Task(task.getTaskName(), task.getServiceName(),
                            task.getMethodName()), task.getTaskCron()));
                }
            } else {
//                on 说明原状态时关闭，现在直接添加
                registrar.addCronTask(new CronTask(new Task(task.getTaskName(), task.getServiceName(),
                        task.getMethodName()), task.getTaskCron()));
            }
        }
        return update > 0;
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