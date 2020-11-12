package com.ym.provider.task.config;

import com.ym.provider.commons.utils.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ymaster1
 * @date 2020/8/14 13:35
 * 由springboot启动后自动回调该方法
 */
@Slf4j
@Component
public class TaskRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //获取applicationContext容器
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        //获取自定义的线程池定时任务执行器,这里为了不跟那个httpclient配置冲突，就把名字定死，不然会报错
        TaskScheduler taskScheduler = applicationContext.getBean(ThreadPoolTaskScheduler.class);
        //执行所有需要执行的任务
        List<ITaskEnum> allTask = TaskUtils.getAllTask();
        System.out.println(allTask.size());
        TaskUtils.getAllTask().forEach(e -> initTask(e,taskScheduler));
    }


    /**
     * 开始执行任务
      * @param TaskEnum
     * @param taskScheduler
     */
    private void initTask(ITaskEnum TaskEnum, TaskScheduler taskScheduler) {
        try {
            ApplicationContext context = ApplicationContextHolder.getApplicationContext();
            taskScheduler.schedule((ITask) context.getBean(TaskEnum.getCode()), new
                    CronTrigger(TaskEnum.getCorn()));
        }catch (Exception e){
            log.info("加载ITaskEnum 异常,{},{}",TaskEnum.getClazz(),TaskEnum.getCode());
            log.error("",e);
        }
    }
}
