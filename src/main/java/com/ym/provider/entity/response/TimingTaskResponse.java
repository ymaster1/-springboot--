package com.ym.provider.entity.response;

import com.ym.provider.entity.UserInfo;
import lombok.Data;

/**
 * @author ymaster1
 * @date 2020/11/18 16:12
 * @description
 */
@Data
public class TimingTaskResponse {
    private UserInfo info;
    private Integer sum;
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 方法所在的service
     */
    private String serviceName;

    /**
     * 任务启用状态 0 停用   1  启用
     */
    private Boolean taskStatus;

    /**
     * 时间表达式
     */
    private String taskCron;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 方法名称
     */
    private String methodName;
}
