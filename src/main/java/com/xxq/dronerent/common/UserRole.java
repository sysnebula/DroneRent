package com.xxq.dronerent.common;

import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author xxq
 * @since 2024-01-01
 */
@Getter
public enum UserRole {

    /**
     * 管理员
     */
    ADMIN("ADMIN", "管理员"),

    /**
     * 员工
     */
    EMPLOYEE("EMPLOYEE", "员工");

    private final String code;
    private final String name;

    UserRole(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据 code 获取角色
     */
    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("无效的角色: " + code);
    }
}
