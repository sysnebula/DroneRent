package com.xxq.dronerent.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxq.dronerent.common.BusinessException;
import com.xxq.dronerent.common.Constants;
import com.xxq.dronerent.common.PageResult;
import com.xxq.dronerent.common.Result;
import com.xxq.dronerent.dto.DroneDTO;
import com.xxq.dronerent.entity.Drone;
import com.xxq.dronerent.service.DroneService;
import com.xxq.dronerent.vo.DroneVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 无人机管理控制器
 * 提供无人机的增删改查、状态管理等接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Slf4j
@Tag(name = "无人机管理", description = "无人机设备的增删改查和状态管理接口")
@RestController
@RequestMapping("/api/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    /**
     * 分页查询无人机列表
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param brand    品牌（可选）
     * @param model    型号（可选）
     * @param status   状态（可选）
     * @return 分页结果
     */
    @Operation(summary = "分页查询无人机列表", description = "支持按品牌、型号、状态筛选")
    @GetMapping("/page")
    public Result<PageResult<DroneVO>> pageDrones(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "品牌") @RequestParam(required = false) String brand,
            @Parameter(description = "型号") @RequestParam(required = false) String model,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        
        log.info("分页查询无人机: pageNum={}, pageSize={}, brand={}, model={}, status={}", 
                pageNum, pageSize, brand, model, status);
        
        // 构建查询条件
        LambdaQueryWrapper<Drone> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(brand != null, Drone::getBrand, brand)
               .like(model != null, Drone::getModel, model)
               .eq(status != null, Drone::getStatus, status)
               .orderByDesc(Drone::getCreateTime);
        
        // 分页查询
        Page<Drone> page = new Page<>(pageNum, pageSize);
        Page<Drone> dronePage = droneService.page(page, wrapper);
        
        // 转换为 VO
        List<DroneVO> droneVOList = dronePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        PageResult<DroneVO> result = PageResult.build(
                dronePage.getTotal(),
                dronePage.getCurrent(),
                dronePage.getSize(),
                droneVOList
        );
        
        return Result.success(result);
    }

    /**
     * 根据ID查询无人机详情
     *
     * @param id 无人机ID
     * @return 无人机信息
     */
    @Operation(summary = "查询无人机详情", description = "根据无人机ID查询详细信息")
    @GetMapping("/{id}")
    public Result<DroneVO> getDroneById(
            @Parameter(description = "无人机ID") @PathVariable Long id) {
        
        Drone drone = droneService.getById(id);
        if (drone == null) {
            throw new BusinessException("无人机不存在");
        }
        
        return Result.success(convertToVO(drone));
    }

    /**
     * 新增无人机
     *
     * @param droneDTO 无人机信息
     * @return 操作结果
     */
    @Operation(summary = "新增无人机", description = "添加新的无人机设备")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> createDrone(@Valid @RequestBody DroneDTO droneDTO) {
        log.info("新增无人机: {}", droneDTO.getDroneNo());
        
        // 验证设备编号唯一性
        if (!droneService.validateDroneNoUnique(droneDTO.getDroneNo(), null)) {
            throw new BusinessException("设备编号已存在: " + droneDTO.getDroneNo());
        }
        
        // 验证序列号唯一性（如果提供了序列号）
        if (droneDTO.getSerialNumber() != null && !droneDTO.getSerialNumber().isEmpty()) {
            LambdaQueryWrapper<Drone> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Drone::getSerialNumber, droneDTO.getSerialNumber());
            if (droneService.count(wrapper) > 0) {
                throw new BusinessException("序列号已存在: " + droneDTO.getSerialNumber());
            }
        }
        
        // 转换为实体
        Drone drone = new Drone();
        BeanUtils.copyProperties(droneDTO, drone);
        
        // 设置默认状态为空闲
        if (drone.getStatus() == null || drone.getStatus().isEmpty()) {
            drone.setStatus(Constants.DRONE_STATUS_IDLE);
        }
        
        // 初始化飞行数据
        if (drone.getFlightHours() == null) {
            drone.setFlightHours(java.math.BigDecimal.ZERO);
        }
        if (drone.getTotalFlights() == null) {
            drone.setTotalFlights(0);
        }
        
        // 保存
        droneService.save(drone);
        
        return Result.success("创建成功", null);
    }

    /**
     * 修改无人机信息
     *
     * @param id       无人机ID
     * @param droneDTO 无人机信息
     * @return 操作结果
     */
    @Operation(summary = "修改无人机信息", description = "更新无人机设备信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> updateDrone(
            @Parameter(description = "无人机ID") @PathVariable Long id,
            @Valid @RequestBody DroneDTO droneDTO) {
        
        log.info("修改无人机信息: id={}", id);
        
        // 检查无人机是否存在
        Drone existDrone = droneService.getById(id);
        if (existDrone == null) {
            throw new BusinessException("无人机不存在");
        }
        
        // 验证设备编号唯一性（排除当前记录）
        if (!droneService.validateDroneNoUnique(droneDTO.getDroneNo(), id)) {
            throw new BusinessException("设备编号已存在: " + droneDTO.getDroneNo());
        }
        
        // 验证序列号唯一性（如果提供了序列号且与原来不同）
        if (droneDTO.getSerialNumber() != null && !droneDTO.getSerialNumber().isEmpty()) {
            if (!droneDTO.getSerialNumber().equals(existDrone.getSerialNumber())) {
                LambdaQueryWrapper<Drone> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Drone::getSerialNumber, droneDTO.getSerialNumber());
                if (droneService.count(wrapper) > 0) {
                    throw new BusinessException("序列号已存在: " + droneDTO.getSerialNumber());
                }
            }
        }
        
        // 更新字段（不更新状态，状态通过专门的接口更新）
        BeanUtils.copyProperties(droneDTO, existDrone, "status");
        
        // 保存
        droneService.updateById(existDrone);
        
        return Result.success("更新成功", null);
    }

    /**
     * 删除无人机
     *
     * @param id 无人机ID
     * @return 操作结果
     */
    @Operation(summary = "删除无人机", description = "根据ID删除无人机（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> deleteDrone(
            @Parameter(description = "无人机ID") @PathVariable Long id) {
        
        log.info("删除无人机: id={}", id);
        
        Drone drone = droneService.getById(id);
        if (drone == null) {
            throw new BusinessException("无人机不存在");
        }
        
        // 检查状态：只有空闲或报废的无人机才能删除
        if (!Constants.DRONE_STATUS_IDLE.equals(drone.getStatus()) &&
            !Constants.DRONE_STATUS_SCRAPPED.equals(drone.getStatus())) {
            throw new BusinessException("只能删除空闲或报废状态的无人机");
        }
        
        // 删除（逻辑删除）
        droneService.removeById(id);
        
        return Result.success("删除成功", null);
    }

    /**
     * 批量删除无人机
     *
     * @param ids 无人机ID列表
     * @return 操作结果
     */
    @Operation(summary = "批量删除无人机", description = "批量删除多个无人机（逻辑删除）")
    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> batchDeleteDrones(
            @Parameter(description = "无人机ID列表") @RequestBody List<Long> ids) {
        
        log.info("批量删除无人机: count={}", ids.size());
        
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的无人机");
        }
        
        // 检查每个无人机的状态
        List<Drone> drones = droneService.listByIds(ids);
        for (Drone drone : drones) {
            if (!Constants.DRONE_STATUS_IDLE.equals(drone.getStatus()) &&
                !Constants.DRONE_STATUS_SCRAPPED.equals(drone.getStatus())) {
                throw new BusinessException(
                    String.format("无人机 %s 处于 %s 状态，不能删除", 
                            drone.getDroneNo(), drone.getStatus())
                );
            }
        }
        
        // 批量删除
        droneService.removeByIds(ids);
        
        return Result.success(String.format("成功删除 %d 个无人机", ids.size()), null);
    }

    /**
     * 更新无人机状态
     *
     * @param id        无人机ID
     * @param newStatus 新状态
     * @return 操作结果
     */
    @Operation(summary = "更新无人机状态", description = "修改无人机的状态（需符合状态转换规则）")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> updateDroneStatus(
            @Parameter(description = "无人机ID") @PathVariable Long id,
            @Parameter(description = "新状态（IDLE/RENTED/MAINTENANCE/SCRAPPED）") 
            @RequestParam String newStatus) {
        
        log.info("更新无人机状态: id={}, newStatus={}", id, newStatus);
        
        // 验证状态值是否合法
        List<String> validStatuses = Arrays.asList(
                Constants.DRONE_STATUS_IDLE,
                Constants.DRONE_STATUS_RENTED,
                Constants.DRONE_STATUS_MAINTENANCE,
                Constants.DRONE_STATUS_SCRAPPED
        );
        
        if (!validStatuses.contains(newStatus)) {
            throw new BusinessException("无效的状态值: " + newStatus);
        }
        
        // 调用 Service 层更新状态（包含状态转换验证）
        droneService.updateDroneStatus(id, newStatus);
        
        return Result.success("状态更新成功", null);
    }

    /**
     * 将 Drone 转换为 DroneVO
     */
    private DroneVO convertToVO(Drone drone) {
        DroneVO vo = new DroneVO();
        BeanUtils.copyProperties(drone, vo);
        return vo;
    }
}
