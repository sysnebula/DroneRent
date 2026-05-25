package com.xxq.dronerent.controller;

import com.xxq.dronerent.common.JwtUtil;
import com.xxq.dronerent.common.Result;
import com.xxq.dronerent.dto.LoginDTO;
import com.xxq.dronerent.security.SecurityUser;
import com.xxq.dronerent.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 提供登录、登出等认证相关接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Slf4j
@Tag(name = "认证管理", description = "用户登录、登出等认证接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 用户登录
     *
     * @param loginDTO 登录请求
     * @return JWT Token 和用户信息
     */
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，返回 JWT Token")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO.getUsername());
        
        try {
            // 认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
            
            // 获取用户信息
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            
            // 生成 Token
            String token = jwtUtil.generateToken(securityUser.getUserId(), securityUser.getUsername());
            
            // 构建响应
            LoginVO loginVO = new LoginVO();
            loginVO.setToken(token);
            loginVO.setExpiresIn(expiration / 1000); // 转换为秒
            loginVO.setUserId(securityUser.getUserId());
            loginVO.setUsername(securityUser.getUsername());
            loginVO.setRealName(securityUser.getRealName());
            loginVO.setRole(securityUser.getRole());
            
            log.info("用户登录成功: {}", loginDTO.getUsername());
            return Result.success("登录成功", loginVO);
            
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 获取当前用户信息
     *
     * @param authentication 认证对象（由 Spring Security 自动注入）
     * @return 当前用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/info")
    public Result<SecurityUser> getCurrentUserInfo(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return Result.success(securityUser);
    }

    /**
     * 刷新 Token
     *
     * @param authentication 认证对象
     * @return 新的 Token
     */
    @Operation(summary = "刷新 Token", description = "刷新当前用户的 JWT Token")
    @PostMapping("/refresh")
    public Result<LoginVO> refreshToken(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        
        // 刷新 Token
        String oldToken = null; // 从请求头获取旧 Token
        String newToken = jwtUtil.refreshToken(oldToken);
        
        if (newToken != null) {
            LoginVO loginVO = new LoginVO();
            loginVO.setToken(newToken);
            loginVO.setExpiresIn(expiration / 1000);
            loginVO.setUserId(securityUser.getUserId());
            loginVO.setUsername(securityUser.getUsername());
            loginVO.setRealName(securityUser.getRealName());
            loginVO.setRole(securityUser.getRole());
            
            return Result.success("Token 刷新成功", loginVO);
        } else {
            return Result.error("Token 刷新失败");
        }
    }
}
