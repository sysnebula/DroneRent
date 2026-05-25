package com.xxq.dronerent.common;

/**
 * 系统常量类
 * 定义系统中使用的各种常量
 *
 * @author yourcompany
 * @since 2024-01-01
 */
public class Constants {

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    /**
     * 登录成功状态码
     */
    public static final Integer LOGIN_SUCCESS = 200;

    /**
     * 登录失败状态码
     */
    public static final Integer LOGIN_ERROR = 500;

    /**
     * 验证码有效期（分钟）
     */
    public static final Long CAPTCHA_EXPIRATION = 5L;

    /**
     * JWT 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * JWT 令牌Header
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 用户ID字段名
     */
    public static final String USER_ID = "userId";

    /**
     * 用户名字段名
     */
    public static final String USERNAME = "username";

    /**
     * 角色权限字段名
     */
    public static final String AUTHORITIES = "authorities";

    /**
     * 无人机状态 - 空闲
     */
    public static final String DRONE_STATUS_IDLE = "IDLE";

    /**
     * 无人机状态 - 出租中
     */
    public static final String DRONE_STATUS_RENTED = "RENTED";

    /**
     * 无人机状态 - 维修中
     */
    public static final String DRONE_STATUS_MAINTENANCE = "MAINTENANCE";

    /**
     * 无人机状态 - 报废
     */
    public static final String DRONE_STATUS_SCRAPPED = "SCRAPPED";

    /**
     * 订单状态 - 待支付
     */
    public static final String ORDER_STATUS_PENDING = "PENDING";

    /**
     * 订单状态 - 已支付
     */
    public static final String ORDER_STATUS_PAID = "PAID";

    /**
     * 订单状态 - 租赁中
     */
    public static final String ORDER_STATUS_RENTING = "RENTING";

    /**
     * 订单状态 - 已完成
     */
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";

    /**
     * 订单状态 - 已取消
     */
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    /**
     * 订单状态 - 已退款
     */
    public static final String ORDER_STATUS_REFUNDED = "REFUNDED";

    /**
     * Redis Key 前缀 - 用户token
     */
    public static final String REDIS_KEY_USER_TOKEN = "user:token:";

    /**
     * Redis Key 前缀 - 验证码
     */
    public static final String REDIS_KEY_CAPTCHA = "captcha:";

    /**
     * Redis Key 前缀 - 无人机信息
     */
    public static final String REDIS_KEY_DRONE = "drone:";

    /**
     * Redis Key 前缀 - 订单信息
     */
    public static final String REDIS_KEY_ORDER = "order:";
}
