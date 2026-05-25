package com.xxq.dronerent.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果类
 * 用于封装所有接口的返回数据，确保前后端交互格式统一
 *
 * @param <T> 返回数据类型
 * @author yourcompany
 * @since 2024-01-01
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 成功返回（自定义消息和数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 失败返回（默认消息）
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败", null);
    }

    /**
     * 失败返回（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * 失败返回（自定义状态码和消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 参数校验失败返回
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * 未授权返回
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    /**
     * 禁止访问返回
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    /**
     * 资源不存在返回
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }
}
