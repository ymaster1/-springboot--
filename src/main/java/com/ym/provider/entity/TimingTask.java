package com.ym.provider.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ym.provider.commons.tkmybais.BaseLogicEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 定时任务表(TimingTask)表实体类
 *
 * @author ymaster1
 * @since 2020-11-13 15:33:36
 */
@Table(name = "timing_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TimingTask extends BaseLogicEntity {
    private static final long serialVersionUID = 325094380719788136L;

    /**
     * 任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 方法所在的service
     */
    @Column(name = "service_name")
    private String serviceName;

    /**
     * 任务启用状态 0 停用   1  启用
     */
    @Column(name = "task_status")
    private Boolean taskStatus;

    /**
     * 时间表达式
     */
    @Column(name = "task_cron")
    private String taskCron;

    /**
     * 任务描述
     */
    @Column(name = "task_desc")
    private String taskDesc;

    /**
     * 方法名称
     */
    @Column(name = "method_name")
    private String methodName;

}