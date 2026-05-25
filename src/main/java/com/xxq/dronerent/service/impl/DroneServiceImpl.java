package com.xxq.dronerent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxq.dronerent.entity.Drone;
import com.xxq.dronerent.mapper.DroneMapper;
import com.xxq.dronerent.service.DroneService;
import org.springframework.stereotype.Service;

/**
 * 无人机设备 Service 实现类
 *
 * @author xxq
 * @since 2024-01-01
 */
@Service
public class DroneServiceImpl extends ServiceImpl<DroneMapper, Drone> implements DroneService {

}
