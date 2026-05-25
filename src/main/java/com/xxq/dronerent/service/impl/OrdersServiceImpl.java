package com.xxq.dronerent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxq.dronerent.entity.Orders;
import com.xxq.dronerent.mapper.OrdersMapper;
import com.xxq.dronerent.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * 订单 Service 实现类
 *
 * @author xxq
 * @since 2024-01-01
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
