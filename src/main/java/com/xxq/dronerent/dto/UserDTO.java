package com.xxq.dronerent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户创建/更新 DTO
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@Schema(description = "用户创建/更新请求")
public class UserDTO {

    /**
     * 用户ID（更新时必填）
     */
    @Schema(description = "用户ID（更新时必填）", example = "1")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码（创建时必填）
     */
    @Schema(description = "密码（创建时必填）", example = "admin123")
    private String password;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "admin@dronerent.com")
    private String email;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 角色（ADMIN-管理员，EMPLOYEE-员工）
     */
    @NotBlank(message = "角色不能为空")
    @Schema(description = "角色（ADMIN-管理员，EMPLOYEE-员工）", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Schema(description = "状态（0-禁用，1-启用）", example = "1")
    private Integer status;
}
