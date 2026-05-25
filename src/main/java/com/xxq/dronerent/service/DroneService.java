package com.xxq.dronerent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxq.dronerent.entity.Drone;

/**
 * 无人机设备 Service 接口
 *
 * @author xxq
 * @since 2024-01-01
 */
public interface DroneService extends IService<Drone> {

    /**
     * 验证设备编号唯一性
     *
     * @param droneNo 设备编号
     * @param excludeId 排除的ID（更新时使用）
     * @return true-唯一，false-不唯一
     */
    boolean validateDroneNoUnique(String droneNo, Long excludeId);

    /**
     * 验证状态转换的合法性
     *
     * @param currentStatus 当前状态
     * @param newStatus 新状态
     * @return true-合法，false-不合法
     */
    boolean validateStatusTransition(String currentStatus, String newStatus);

    /**
     * 更新无人机状态
     *
     * @param id 无人机ID
     * @param newStatus 新状态
     */
    void updateDroneStatus(Long id, String newStatus);
}
