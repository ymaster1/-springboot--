package com.ym.provider.task.es;

import com.ym.provider.commons.redis.LockService;
import com.ym.provider.entity.EsSyncTask;
import com.ym.provider.service.EsSyncTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ymaster1
 * @date 2020/11/11 16:53
 * @description
 */
@Component
@Slf4j
public class TaskRunner implements ApplicationRunner {

    @Autowired
    private LockService lockService;
    @Autowired
    private CronTaskRegistrar registrar;
    @Autowired
    private EsSyncTaskService taskService;

    /**
     * 获取所有生效的任务
     *
     * @return
     */
    private List<EsSyncTask> getTasks() {
        List<EsSyncTask> list = taskService.queryAllActive();
        if (CollectionUtils.isEmpty(list)) {
            log.warn("没有生效的定时任务待注册！");
        }
        return list;
    }

    /**
     * 启动时执行所有生效的任务
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        List<EsSyncTask> tasks = this.getTasks();
        tasks.forEach(e -> {
            Task task = new Task(e.getTaskName(), lockService, e.getSyncTable(), e.getIndexName());
            registrar.addCronTask(task, e.getTaskCron());
        });
        log.info("所有任务已经加载完成！");
    }
}
