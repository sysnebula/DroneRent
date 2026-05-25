package com.xxq.dronerent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxq.dronerent.common.BusinessException;
import com.xxq.dronerent.common.Constants;
import com.xxq.dronerent.entity.Drone;
import com.xxq.dronerent.mapper.DroneMapper;
import com.xxq.dronerent.service.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 无人机设备 Service 实现类
 *
 * @author xxq
 * @since 2024-01-01
 */
@Slf4j
@Service
public class DroneServiceImpl extends ServiceImpl<DroneMapper, Drone> implements DroneService {

    @Override
    public boolean validateDroneNoUnique(String droneNo, Long excludeId) {
        LambdaQueryWrapper<Drone> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Drone::getDroneNo, droneNo);
        
        // 如果是更新操作，排除当前记录
        if (excludeId != null) {
            wrapper.ne(Drone::getId, excludeId);
        }
        
        long count = this.count(wrapper);
        return count == 0;
    }

    @Override
    public boolean validateStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }
        
        // 定义合法的状态转换规则
        // IDLE（空闲）-> RENTED, MAINTENANCE, SCRAPPED
        // RENTED（出租中）-> IDLE, MAINTENANCE, SCRAPPED
        // MAINTENANCE（维修中）-> IDLE, SCRAPPED
        // SCRAPPED（报废）-> (不允许转换)
        
        switch (currentStatus) {
            case Constants.DRONE_STATUS_IDLE:
                // 空闲状态可以转换为：出租中、维修中、报废
                return Constants.DRONE_STATUS_RENTED.equals(newStatus) ||
                       Constants.DRONE_STATUS_MAINTENANCE.equals(newStatus) ||
                       Constants.DRONE_STATUS_SCRAPPED.equals(newStatus);
                       
            case Constants.DRONE_STATUS_RENTED:
                // 出租中可以转换为：空闲（归还）、维修中、报废
                return Constants.DRONE_STATUS_IDLE.equals(newStatus) ||
                       Constants.DRONE_STATUS_MAINTENANCE.equals(newStatus) ||
                       Constants.DRONE_STATUS_SCRAPPED.equals(newStatus);
                       
            case Constants.DRONE_STATUS_MAINTENANCE:
                // 维修中可以转换为：空闲（维修完成）、报废
                return Constants.DRONE_STATUS_IDLE.equals(newStatus) ||
                       Constants.DRONE_STATUS_SCRAPPED.equals(newStatus);
                
            case Constants.DRONE_STATUS_SCRAPPED:
                // 报废后不允许任何状态转换
                return false;
                
            default:
                log.warn("未知的无人机状态: {}", currentStatus);
                return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDroneStatus(Long id, String newStatus) {
        log.info("更新无人机状态: id={}, newStatus={}", id, newStatus);
        
        // 查询无人机
        Drone drone = this.getById(id);
        if (drone == null) {
            throw new BusinessException("无人机不存在");
        }
        
        String currentStatus = drone.getStatus();
        
        // 验证状态转换的合法性
        if (!validateStatusTransition(currentStatus, newStatus)) {
            throw new BusinessException(
                String.format("不允许的状态转换: %s -> %s", currentStatus, newStatus)
            );
        }
        
        // 更新状态
        drone.setStatus(newStatus);
        this.updateById(drone);
        
        log.info("无人机状态更新成功: id={}, {} -> {}", id, currentStatus, newStatus);
    }
}
