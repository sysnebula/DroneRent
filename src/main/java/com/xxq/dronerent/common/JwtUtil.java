package com.xxq.dronerent.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * 用于生成、解析和验证 JWT Token
 *
 * @author yourcompany
 * @since 2024-01-01
 */
@Slf4j
@Component
public class JwtUtil {

    /**
     * JWT 密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT 过期时间（毫秒）
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 获取密钥
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从 Token 中获取 Claims
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("解析 Token 失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 生成 Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return Token
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.USER_ID, userId);
        claims.put(Constants.USERNAME, username);
        return createToken(claims, username);
    }

    /**
     * 创建 Token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSignKey())
                .compact();
    }

    /**
     * 从 Token 中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return Long.valueOf(claims.get(Constants.USER_ID).toString());
        }
        return null;
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return false;
            }
            Date expiration = claims.getExpiration();
            // Token 未过期
            return !expiration.before(new Date());
        } catch (Exception e) {
            log.error("验证 Token 失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 判断 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        }
        return true;
    }

    /**
     * 刷新 Token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Long userId = Long.valueOf(claims.get(Constants.USER_ID).toString());
            String username = claims.getSubject();
            return generateToken(userId, username);
        }
        return null;
    }
}
