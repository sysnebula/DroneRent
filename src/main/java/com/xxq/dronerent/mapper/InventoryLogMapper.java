package com.xxq.dronerent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxq.dronerent.entity.InventoryLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存变动记录 Mapper 接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Mapper
public interface InventoryLogMapper extends BaseMapper<InventoryLog> {

}
