package com.ym.provider.task.config;

/**
 * @author ymaster1
 * @date 2020/8/14 13:39
 */
public interface ITaskEnum {
    String getDesc();

    String getCode();

    String getCorn();

    Class<? extends ITask> getClazz();

}
