package com.xxq.dronerent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务流水实体类
 * 对应数据库表：finance_transaction
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@TableName("finance_transaction")
public class FinanceTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流水号
     */
    private String transactionNo;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 交易类型（RENTAL_FEE-租金，DEPOSIT-押金，DAMAGE_FEE-赔偿费，OVERDUE_FEE-逾期费，REFUND-退款，MAINTENANCE_FEE-维修费）
     */
    private String transactionType;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 方向（INCOME-收入，EXPENSE-支出）
     */
    private String direction;

    /**
     * 支付方式（WECHAT-微信，ALIPAY-支付宝，CASH-现金，CARD-刷卡）
     */
    private String paymentMethod;

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;

    /**
     * 操作人ID
     */
    private Long operatorId;

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
