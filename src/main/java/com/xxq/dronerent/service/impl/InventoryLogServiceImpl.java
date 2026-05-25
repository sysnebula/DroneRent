package com.xxq.dronerent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxq.dronerent.entity.InventoryLog;
import com.xxq.dronerent.mapper.InventoryLogMapper;
import com.xxq.dronerent.service.InventoryLogService;
import org.springframework.stereotype.Service;

/**
 * 库存变动记录 Service 实现类
 *
 * @author xxq
 * @since 2024-01-01
 */
@Service
public class InventoryLogServiceImpl extends ServiceImpl<InventoryLogMapper, InventoryLog> implements InventoryLogService {

}
