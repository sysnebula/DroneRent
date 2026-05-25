package com.xxq.dronerent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 对应数据库表：customer
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@TableName("customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户编号
     */
    private String customerNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 驾驶证/飞行执照号
     */
    private String driverLicenseNo;

    /**
     * 信用分数（满分100）
     */
    private Integer creditScore;

    /**
     * 黑名单标识（0-否，1-是）
     */
    private Integer blacklist;

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
