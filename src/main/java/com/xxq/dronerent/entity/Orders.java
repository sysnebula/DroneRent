package com.xxq.dronerent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 对应数据库表：orders
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 无人机ID
     */
    private Long droneId;

    /**
     * 经办人ID（员工）
     */
    private Long userId;

    /**
     * 租赁开始日期
     */
    private LocalDate startDate;

    /**
     * 租赁结束日期
     */
    private LocalDate endDate;

    /**
     * 实际取机时间
     */
    private LocalDateTime actualStartDate;

    /**
     * 实际还机时间
     */
    private LocalDateTime actualEndDate;

    /**
     * 租赁天数
     */
    private Integer rentalDays;

    /**
     * 日租金
     */
    private BigDecimal dailyPrice;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 押金金额
     */
    private BigDecimal depositAmount;

    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 已退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 损坏赔偿费
     */
    private BigDecimal damageFee;

    /**
     * 逾期费用
     */
    private BigDecimal overdueFee;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 订单状态（PENDING-待支付，PAID-已支付，RENTING-租赁中，COMPLETED-已完成，CANCELLED-已取消，REFUNDED-已退款）
     */
    private String status;

    /**
     * 支付方式（WECHAT-微信，ALIPAY-支付宝，CASH-现金，CARD-刷卡）
     */
    private String paymentMethod;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 取机人姓名
     */
    private String pickupPerson;

    /**
     * 取机人电话
     */
    private String pickupPhone;

    /**
     * 还机人姓名
     */
    private String returnPerson;

    /**
     * 还机人电话
     */
    private String returnPhone;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 取消原因
     */
    private String cancelReason;

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
