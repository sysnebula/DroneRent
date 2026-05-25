package com.xxq.dronerent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 无人机创建/更新 DTO
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@Schema(description = "无人机创建/更新请求")
public class DroneDTO {

    /**
     * 无人机ID（更新时必填）
     */
    @Schema(description = "无人机ID（更新时必填）", example = "1")
    private Long id;

    /**
     * 无人机编号
     */
    @NotBlank(message = "无人机编号不能为空")
    @Schema(description = "无人机编号", example = "DRONE001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String droneNo;

    /**
     * 品牌
     */
    @NotBlank(message = "品牌不能为空")
    @Schema(description = "品牌", example = "DJI", requiredMode = Schema.RequiredMode.REQUIRED)
    private String brand;

    /**
     * 型号
     */
    @NotBlank(message = "型号不能为空")
    @Schema(description = "型号", example = "Mavic 3", requiredMode = Schema.RequiredMode.REQUIRED)
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
    @DecimalMin(value = "0", message = "购买价格不能小于0")
    @Schema(description = "购买价格", example = "12999.00")
    private BigDecimal purchasePrice;

    /**
     * 日租金
     */
    @NotNull(message = "日租金不能为空")
    @DecimalMin(value = "0.01", message = "日租金必须大于0")
    @Schema(description = "日租金", example = "299.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal dailyRentalPrice;

    /**
     * 押金金额
     */
    @NotNull(message = "押金金额不能为空")
    @DecimalMin(value = "0", message = "押金金额不能小于0")
    @Schema(description = "押金金额", example = "3000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal depositAmount;

    /**
     * 状态（IDLE-空闲，RENTED-出租中，MAINTENANCE-维修中，SCRAPPED-报废）
     */
    @Schema(description = "状态（IDLE-空闲，RENTED-出租中，MAINTENANCE-维修中，SCRAPPED-报废）", example = "IDLE")
    private String status;

    /**
     * 描述信息
     */
    @Schema(description = "描述信息", example = "大疆 Mavic 3 专业航拍无人机")
    private String description;

    /**
     * 图片URL列表（JSON数组）
     */
    @Schema(description = "图片URL列表（JSON数组）", example = "[\"https://example.com/image1.jpg\"]")
    private String images;

    /**
     * 规格参数（JSON对象）
     */
    @Schema(description = "规格参数（JSON对象）", example = "{\"maxFlightTime\": \"46分钟\", \"maxSpeed\": \"75km/h\"}")
    private String specs;
}
