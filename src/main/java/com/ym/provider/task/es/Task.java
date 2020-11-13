package com.ym.provider.task.es;


import com.ym.provider.commons.redis.LockService;
import com.ym.provider.commons.utils.ApplicationContextHolder;

/**
 * @author ymaster1
 * @date 2020/11/11 15:30
 * @description 所有的es同步任务都用这个task, 处理逻辑都一样，只是数据不一样
 */
public class Task implements Runnable {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 锁
     */
    private LockService lockService;
    /**
     * 要同步的数据所在的表
     */
    private String table;
    /**
     * 存储数据的es索引
     */
    private String indexName;

    public Task(String taskName, LockService lockService, String table, String indexName) {
        this.lockService = lockService;
        this.taskName = taskName;
        this.table = table;
        this.indexName = indexName;
        this.lockService = ApplicationContextHolder.getBean(LockService.class);
    }

    @Override
    public void run() {
        Boolean lock = lockService.lock(taskName);
        if (lock) {
            try {
// 处理业务
            } finally {
                lockService.unLock(taskName);
            }
        }
    }

    /**
     * @param table     需要同步的表
     * @param indexName elasticsearch 索引
     */
    private void sync(String table, String indexName) {

    }
}
