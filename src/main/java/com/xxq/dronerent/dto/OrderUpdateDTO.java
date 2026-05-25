package com.xxq.dronerent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 订单更新 DTO
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@Schema(description = "订单更新请求")
public class OrderUpdateDTO {

    /**
     * 租赁结束日期（续租时使用）
     */
    @Schema(description = "租赁结束日期（续租时使用）", example = "2024-01-25")
    private LocalDate endDate;

    /**
     * 还机人姓名
     */
    @Schema(description = "还机人姓名", example = "李四")
    private String returnPerson;

    /**
     * 还机人电话
     */
    @Schema(description = "还机人电话", example = "13900139000")
    private String returnPhone;

    /**
     * 订单备注
     */
    @Schema(description = "订单备注", example = "设备完好无损")
    private String remark;
}
