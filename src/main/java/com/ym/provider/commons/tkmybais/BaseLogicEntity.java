package com.ym.provider.commons.tkmybais;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : ymaster1
 * @version : 1.0
 * @date : 2019-01-28 11:10
 * @Description :
 */
@Data
@MappedSuperclass
public class BaseLogicEntity extends BaseObject {
    /**
     * 实体Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20) COMMENT '主键ID,自动生成'")
    @OrderBy(value = "DESC")
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "server_create_time", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '系统创建时间'")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date serverCreateTime;

    /**
     * 更新时间
     */
    @Column(name = "server_update_time", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '系统更新时间'")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date serverUpdateTime;


    /**
     * 是否是可用数据
     */
    @Column(name = "status_flag", columnDefinition = "TINYINT(1) NOT NULL DEFAULT true COMMENT '是否可用'")
    private Boolean statusFlag;
}
