package com.xxq.dronerent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

/**
 * 订单创建 DTO
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@Schema(description = "订单创建请求")
public class OrderCreateDTO {

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    @Schema(description = "客户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long customerId;

    /**
     * 无人机ID
     */
    @NotNull(message = "无人机ID不能为空")
    @Schema(description = "无人机ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long droneId;

    /**
     * 租赁开始日期
     */
    @NotNull(message = "租赁开始日期不能为空")
    @Schema(description = "租赁开始日期", example = "2024-01-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate startDate;

    /**
     * 租赁结束日期
     */
    @NotNull(message = "租赁结束日期不能为空")
    @Schema(description = "租赁结束日期", example = "2024-01-20", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate endDate;

    /**
     * 取机人姓名
     */
    @Schema(description = "取机人姓名", example = "李四")
    private String pickupPerson;

    /**
     * 取机人电话
     */
    @Schema(description = "取机人电话", example = "13900139000")
    private String pickupPhone;

    /**
     * 订单备注
     */
    @Schema(description = "订单备注", example = "请确保设备完好")
    private String remark;
}
