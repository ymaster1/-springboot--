package com.ym.provider.task.table;

import java.util.concurrent.ScheduledFuture;

/**
 * @author ymaster1
 * @date 2020/11/11 16:36
 * @description 保存TaskScheduler.schedule的返回值，可以用于取消或者修改任务
 */
public class ScheduledTask {

    volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
