package com.xxq.dronerent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxq.dronerent.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单 Mapper 接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}
