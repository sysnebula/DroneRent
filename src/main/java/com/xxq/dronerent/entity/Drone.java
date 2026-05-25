package com.xxq.dronerent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 无人机设备实体类
 * 对应数据库表：drone
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@TableName("drone")
public class Drone implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 无人机ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 无人机编号
     */
    private String droneNo;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 型号
     */
    private String model;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 购买日期
     */
    private LocalDate purchaseDate;

    /**
     * 购买价格
     */
    private BigDecimal purchasePrice;

    /**
     * 日租金
     */
    private BigDecimal dailyRentalPrice;

    /**
     * 押金金额
     */
    private BigDecimal depositAmount;

    /**
     * 状态（IDLE-空闲，RENTED-出租中，MAINTENANCE-维修中，SCRAPPED-报废）
     */
    private String status;

    /**
     * 累计飞行时长（小时）
     */
    private BigDecimal flightHours;

    /**
     * 累计飞行次数
     */
    private Integer totalFlights;

    /**
     * 上次维护日期
     */
    private LocalDate lastMaintenanceDate;

    /**
     * 下次维护日期
     */
    private LocalDate nextMaintenanceDate;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 图片URL列表（JSON数组）
     */
    private String images;

    /**
     * 规格参数（JSON对象）
     */
    private String specs;

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
