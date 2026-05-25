package com.xxq.dronerent.controller;

import com.xxq.dronerent.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于验证项目配置是否正确
 *
 * @author xxq
 * @since 2024-01-01
 */
@Tag(name = "测试接口", description = "用于验证项目配置的测试接口")
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * 健康检查接口
     */
    @Operation(summary = "健康检查", description = "检查系统是否正常运行")
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("message", "系统运行正常");
        
        return Result.success(data);
    }

    /**
     * 测试统一返回格式
     */
    @Operation(summary = "测试成功返回", description = "测试统一返回结果格式")
    @GetMapping("/success")
    public Result<String> testSuccess() {
        return Result.success("操作成功！");
    }

    /**
     * 测试带数据的返回
     */
    @Operation(summary = "测试带数据返回", description = "测试统一返回结果格式（带数据）")
    @GetMapping("/data")
    public Result<Map<String, Object>> testData() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "无人机出租系统");
        data.put("version", "1.0.0");
        data.put("time", LocalDateTime.now());
        
        return Result.success(data);
    }
}
