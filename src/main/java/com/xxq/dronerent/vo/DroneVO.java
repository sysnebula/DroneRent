package com.xxq.dronerent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 无人机信息 VO
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@Schema(description = "无人机信息")
public class DroneVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 无人机ID
     */
    @Schema(description = "无人机ID", example = "1")
    private Long id;

    /**
     * 无人机编号
     */
    @Schema(description = "无人机编号", example = "DRONE001")
    private String droneNo;

    /**
     * 品牌
     */
    @Schema(description = "品牌", example = "DJI")
    private String brand;

    /**
     * 型号
     */
    @Schema(description = "型号", example = "Mavic 3")
    private String model;

    /**
     * 序列号
     */
    @Schema(description = "序列号", example = "SN2024001")
    private String serialNumber;

    /**
     * 购买日期
     */
    @Schema(description = "购买日期", example = "2024-01-15")
    private LocalDate purchaseDate;

    /**
     * 购买价格
     */
    @Schema(description = "购买价格", example = "12999.00")
    private BigDecimal purchasePrice;

    /**
     * 日租金
     */
    @Schema(description = "日租金", example = "299.00")
    private BigDecimal dailyRentalPrice;

    /**
     * 押金金额
     */
    @Schema(description = "押金金额", example = "3000.00")
    private BigDecimal depositAmount;

    /**
     * 状态（IDLE-空闲，RENTED-出租中，MAINTENANCE-维修中，SCRAPPED-报废）
     */
    @Schema(description = "状态", example = "IDLE")
    private String status;

    /**
     * 累计飞行时长（小时）
     */
    @Schema(description = "累计飞行时长（小时）", example = "50.50")
    private BigDecimal flightHours;

    /**
     * 累计飞行次数
     */
    @Schema(description = "累计飞行次数", example = "120")
    private Integer totalFlights;

    /**
     * 上次维护日期
     */
    @Schema(description = "上次维护日期", example = "2024-01-01")
    private LocalDate lastMaintenanceDate;

    /**
     * 下次维护日期
     */
    @Schema(description = "下次维护日期", example = "2024-07-01")
    private LocalDate nextMaintenanceDate;

    /**
     * 描述信息
     */
    @Schema(description = "描述信息", example = "大疆 Mavic 3 专业航拍无人机")
    private String description;

    /**
     * 图片URL列表（JSON数组）
     */
    @Schema(description = "图片URL列表", example = "[\"https://example.com/image1.jpg\"]")
    private String images;

    /**
     * 规格参数（JSON对象）
     */
    @Schema(description = "规格参数", example = "{\"maxFlightTime\": \"46分钟\"}")
    private String specs;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
