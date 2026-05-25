package com.xxq.dronerent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxq.dronerent.entity.Drone;
import org.apache.ibatis.annotations.Mapper;

/**
 * 无人机设备 Mapper 接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Mapper
public interface DroneMapper extends BaseMapper<Drone> {

}
