package com.ym.provider.entity;

import com.ym.provider.commons.tkmybais.BaseLogicEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * es数据同步任务表(EsSyncTask)表实体类
 *
 * @author ymaster1
 * @since 2020-11-12 14:19:37
 */
@Table(name = "es_sync_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EsSyncTask extends BaseLogicEntity {
    private static final long serialVersionUID = -54764577275307717L;

    /**
     * 任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 同步表
     */
    @Column(name = "sync_table")
    private String syncTable;

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
     * es索引名称
     */
    @Column(name = "index_name")
    private String indexName;

}