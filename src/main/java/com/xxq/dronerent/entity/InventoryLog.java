package com.xxq.dronerent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存变动记录实体类
 * 对应数据库表：inventory_log
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@TableName("inventory_log")
public class InventoryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 无人机ID
     */
    private Long droneId;

    /**
     * 变动类型（PURCHASE-采购，RENT_OUT-出租，RETURN-归还，MAINTENANCE-维修，SCRAP-报废）
     */
    private String changeType;

    /**
     * 原状态
     */
    private String oldStatus;

    /**
     * 新状态
     */
    private String newStatus;

    /**
     * 关联订单号
     */
    private String relatedOrderNo;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

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
}
