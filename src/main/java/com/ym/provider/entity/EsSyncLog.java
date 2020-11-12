package com.ym.provider.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * es数据同步任务表日志表(EsSyncLog)表实体类
 *
 * @author ymaster1
 * @since 2020-11-12 14:56:12
 */
@Table(name = "es_sync_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EsSyncLog extends BaseLogicEntity {
    private static final long serialVersionUID = 347626040810241707L;

    /**
     * 主键id
     */
    @Column(name = "id")
    private Long id;

    /**
     * 同步时间
     */
    @Column(name = "sync_time")
    private Date syncTime;

    /**
     * 同步表
     */
    @Column(name = "sync_table")
    private String syncTable;

    /**
     * 数据sql
     */
    @Column(name = "sync_sql")
    private String syncSql;

    /**
     * 增量数据 0-add 1-update 2-del
     */
    @Column(name = "sync_type")
    private Integer syncType;

    /**
     * 1-success  0-fail
     */
    @Column(name = "result_flag")
    private Integer resultFlag;

    /**
     * 错误日志
     */
    @Column(name = "fail_log")
    private String failLog;

    /**
     * 是否可用 0 不可用 1可用
     */
    @Column(name = "status_flag")
    private Integer statusFlag;

    /**
     * 系统创建时间
     */
    @Column(name = "server_create_time")
    private Date serverCreateTime;

    /**
     * 系统更新时间
     */
    @Column(name = "server_update_time")
    private Date serverUpdateTime;
}