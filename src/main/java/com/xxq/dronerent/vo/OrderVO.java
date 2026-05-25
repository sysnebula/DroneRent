package com.xxq.dronerent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单信息 VO
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@Schema(description = "订单信息")
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID", example = "1")
    private Long id;

    /**
     * 订单编号
     */
    @Schema(description = "订单编号", example = "ORD20240115001")
    private String orderNo;

    /**
     * 客户ID
     */
    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    /**
     * 客户姓名
     */
    @Schema(description = "客户姓名", example = "李四")
    private String customerName;

    /**
     * 无人机ID
     */
    @Schema(description = "无人机ID", example = "1")
    private Long droneId;

    /**
     * 无人机编号
     */
    @Schema(description = "无人机编号", example = "DRONE001")
    private String droneNo;

    /**
     * 无人机型号
     */
    @Schema(description = "无人机型号", example = "DJI Mavic 3")
    private String droneModel;

    /**
     * 经办人ID（员工）
     */
    @Schema(description = "经办人ID", example = "1")
    private Long userId;

    /**
     * 经办人姓名
     */
    @Schema(description = "经办人姓名", example = "张三")
    private String userName;

    /**
     * 租赁开始日期
     */
    @Schema(description = "租赁开始日期", example = "2024-01-15")
    private LocalDate startDate;

    /**
     * 租赁结束日期
     */
    @Schema(description = "租赁结束日期", example = "2024-01-20")
    private LocalDate endDate;

    /**
     * 实际取机时间
     */
    @Schema(description = "实际取机时间")
    private LocalDateTime actualStartDate;

    /**
     * 实际还机时间
     */
    @Schema(description = "实际还机时间")
    private LocalDateTime actualEndDate;

    /**
     * 租赁天数
     */
    @Schema(description = "租赁天数", example = "5")
    private Integer rentalDays;

    /**
     * 日租金
     */
    @Schema(description = "日租金", example = "299.00")
    private BigDecimal dailyPrice;

    /**
     * 订单总金额
     */
    @Schema(description = "订单总金额", example = "1495.00")
    private BigDecimal totalAmount;

    /**
     * 押金金额
     */
    @Schema(description = "押金金额", example = "3000.00")
    private BigDecimal depositAmount;

    /**
     * 已支付金额
     */
    @Schema(description = "已支付金额", example = "4495.00")
    private BigDecimal paidAmount;

    /**
     * 已退款金额
     */
    @Schema(description = "已退款金额", example = "0.00")
    private BigDecimal refundAmount;

    /**
     * 损坏赔偿费
     */
    @Schema(description = "损坏赔偿费", example = "0.00")
    private BigDecimal damageFee;

    /**
     * 逾期费用
     */
    @Schema(description = "逾期费用", example = "0.00")
    private BigDecimal overdueFee;

    /**
     * 优惠金额
     */
    @Schema(description = "优惠金额", example = "0.00")
    private BigDecimal discountAmount;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态", example = "PENDING")
    private String status;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式", example = "WECHAT")
    private String paymentMethod;

    /**
     * 支付时间
     */
    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

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
    @Schema(description = "订单备注", example = "请确保设备完好")
    private String remark;

    /**
     * 取消原因
     */
    @Schema(description = "取消原因")
    private String cancelReason;

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
