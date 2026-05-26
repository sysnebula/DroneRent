package com.xxq.dronerent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxq.dronerent.dto.OrderCreateDTO;
import com.xxq.dronerent.entity.Orders;

/**
 * 订单 Service 接口
 *
 * @author xxq
 * @since 2024-01-01
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 创建订单
     *
     * @param orderCreateDTO 订单创建请求
     * @param userId 经办人ID
     * @return 订单ID
     */
    Long createOrder(OrderCreateDTO orderCreateDTO, Long userId);

    /**
     * 续租订单
     *
     * @param orderId 订单ID
     * @param newEndDate 新的结束日期
     */
    void extendOrder(Long orderId, java.time.LocalDate newEndDate);

    /**
     * 提前归还
     *
     * @param orderId 订单ID
     * @param returnPerson 还机人姓名
     * @param returnPhone 还机人电话
     */
    void earlyReturn(Long orderId, String returnPerson, String returnPhone);

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @param paymentMethod 支付方式
     */
    void payOrder(Long orderId, String paymentMethod);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param cancelReason 取消原因
     */
    void cancelOrder(Long orderId, String cancelReason);
}
