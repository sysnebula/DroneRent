package com.xxq.dronerent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录响应 VO
 *
 * @author xxq
 * @since 2024-01-01
 */
@Data
@Schema(description = "登录响应")
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JWT Token
     */
    @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    /**
     * Token 类型
     */
    @Schema(description = "Token 类型", example = "Bearer")
    private String tokenType;

    /**
     * 过期时间（秒）
     */
    @Schema(description = "过期时间（秒）", example = "86400")
    private Long expiresIn;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", example = "系统管理员")
    private String realName;

    /**
     * 角色
     */
    @Schema(description = "角色", example = "ADMIN")
    private String role;

    public LoginVO() {
        this.tokenType = "Bearer";
    }
}
