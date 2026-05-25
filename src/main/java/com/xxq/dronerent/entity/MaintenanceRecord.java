package com.xxq.dronerent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 维修记录实体类
 * 对应数据库表：maintenance_record
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@TableName("maintenance_record")
public class MaintenanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 无人机ID
     */
    private Long droneId;

    /**
     * 维修类型（ROUTINE-常规保养，REPAIR-故障维修，INSPECTION-检查）
     */
    private String maintenanceType;

    /**
     * 维修开始时间
     */
    private LocalDateTime startDate;

    /**
     * 维修结束时间
     */
    private LocalDateTime endDate;

    /**
     * 维修费用
     */
    private BigDecimal maintenanceCost;

    /**
     * 维修内容
     */
    private String maintenanceContent;

    /**
     * 故障描述
     */
    private String faultDescription;

    /**
     * 解决方案
     */
    private String solution;

    /**
     * 更换零件（JSON数组）
     */
    private String partsReplaced;

    /**
     * 维修技师
     */
    private String technician;

    /**
     * 状态（IN_PROGRESS-维修中，COMPLETED-已完成，CANCELLED-已取消）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
