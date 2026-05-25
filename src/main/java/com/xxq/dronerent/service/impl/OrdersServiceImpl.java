package com.xxq.dronerent.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxq.dronerent.common.BusinessException;
import com.xxq.dronerent.common.Constants;
import com.xxq.dronerent.dto.OrderCreateDTO;
import com.xxq.dronerent.entity.Customer;
import com.xxq.dronerent.entity.Drone;
import com.xxq.dronerent.entity.Orders;
import com.xxq.dronerent.mapper.OrdersMapper;
import com.xxq.dronerent.service.CustomerService;
import com.xxq.dronerent.service.DroneService;
import com.xxq.dronerent.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 订单 Service 实现类
 *
 * @author xxq
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    private final DroneService droneService;
    private final CustomerService customerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(OrderCreateDTO orderCreateDTO, Long userId) {
        log.info("创建订单: customerId={}, droneId={}, startDate={}, endDate={}",
                orderCreateDTO.getCustomerId(), orderCreateDTO.getDroneId(),
                orderCreateDTO.getStartDate(), orderCreateDTO.getEndDate());

        // 1. 验证客户是否存在
        Customer customer = customerService.getById(orderCreateDTO.getCustomerId());
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        // 检查客户是否在黑名单中
        if (customer.getBlacklist() != null && customer.getBlacklist() == 1) {
            throw new BusinessException("该客户已被列入黑名单，无法创建订单");
        }

        // 2. 验证无人机是否存在
        Drone drone = droneService.getById(orderCreateDTO.getDroneId());
        if (drone == null) {
            throw new BusinessException("无人机不存在");
        }

        // 3. 检查无人机状态是否为空闲
        if (!Constants.DRONE_STATUS_IDLE.equals(drone.getStatus())) {
            throw new BusinessException(
                    String.format("无人机当前状态为 %s，无法出租", drone.getStatus())
            );
        }

        // 4. 验证租期
        LocalDate startDate = orderCreateDTO.getStartDate();
        LocalDate endDate = orderCreateDTO.getEndDate();

        if (startDate.isBefore(LocalDate.now())) {
            throw new BusinessException("租赁开始日期不能早于今天");
        }

        if (endDate.isBefore(startDate)) {
            throw new BusinessException("租赁结束日期不能早于开始日期");
        }

        // 计算租赁天数
        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; // 包含首尾两天
        if (rentalDays <= 0) {
            throw new BusinessException("租赁天数必须大于0");
        }

        // 5. 计算费用
        BigDecimal dailyPrice = drone.getDailyRentalPrice();
        BigDecimal depositAmount = drone.getDepositAmount();
        BigDecimal totalAmount = dailyPrice.multiply(BigDecimal.valueOf(rentalDays));

        log.info("订单费用计算: 租赁天数={}, 日租金={}, 总金额={}, 押金={}",
                rentalDays, dailyPrice, totalAmount, depositAmount);

        // 6. 生成订单编号
        String orderNo = generateOrderNo();

        // 7. 创建订单
        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setCustomerId(customer.getId());
        order.setDroneId(drone.getId());
        order.setUserId(userId);
        order.setStartDate(startDate);
        order.setEndDate(endDate);
        order.setRentalDays((int) rentalDays);
        order.setDailyPrice(dailyPrice);
        order.setTotalAmount(totalAmount);
        order.setDepositAmount(depositAmount);
        order.setPaidAmount(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setDamageFee(BigDecimal.ZERO);
        order.setOverdueFee(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setStatus(Constants.ORDER_STATUS_PENDING); // 待支付
        order.setPickupPerson(orderCreateDTO.getPickupPerson());
        order.setPickupPhone(orderCreateDTO.getPickupPhone());
        order.setRemark(orderCreateDTO.getRemark());

        this.save(order);

        // 8. 更新无人机状态为出租中
        droneService.updateDroneStatus(drone.getId(), Constants.DRONE_STATUS_RENTED);

        log.info("订单创建成功: orderId={}, orderNo={}", order.getId(), orderNo);

        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void extendOrder(Long orderId, LocalDate newEndDate) {
        log.info("续租订单: orderId={}, newEndDate={}", orderId, newEndDate);

        // 1. 查询订单
        Orders order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 2. 验证订单状态
        if (!Constants.ORDER_STATUS_PAID.equals(order.getStatus()) &&
            !Constants.ORDER_STATUS_RENTING.equals(order.getStatus())) {
            throw new BusinessException("只有已支付或租赁中的订单才能续租");
        }

        // 3. 验证新的结束日期
        if (newEndDate.isBefore(order.getEndDate())) {
            throw new BusinessException("新的结束日期不能早于原结束日期");
        }

        // 4. 计算新增的天数和费用
        long additionalDays = ChronoUnit.DAYS.between(order.getEndDate(), newEndDate);
        BigDecimal additionalAmount = order.getDailyPrice().multiply(BigDecimal.valueOf(additionalDays));

        // 5. 更新订单
        order.setEndDate(newEndDate);
        order.setRentalDays(order.getRentalDays() + (int) additionalDays);
        order.setTotalAmount(order.getTotalAmount().add(additionalAmount));

        this.updateById(order);

        log.info("订单续租成功: orderId={}, 新增天数={}, 新增费用={}",
                orderId, additionalDays, additionalAmount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void earlyReturn(Long orderId, String returnPerson, String returnPhone) {
        log.info("提前归还: orderId={}", orderId);

        // 1. 查询订单
        Orders order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 2. 验证订单状态
        if (!Constants.ORDER_STATUS_RENTING.equals(order.getStatus())) {
            throw new BusinessException("只有租赁中的订单才能归还");
        }

        // 3. 计算实际租赁天数和费用
        LocalDateTime now = LocalDateTime.now();
        long actualDays = ChronoUnit.DAYS.between(order.getStartDate(), now.toLocalDate()) + 1;

        // 如果实际天数少于计划天数，按实际天数计算；否则按计划天数计算
        int chargeableDays = Math.min((int) actualDays, order.getRentalDays());
        BigDecimal actualAmount = order.getDailyPrice().multiply(BigDecimal.valueOf(chargeableDays));

        // 计算退款金额（如果提前归还）
        BigDecimal refundAmount = BigDecimal.ZERO;
        if (actualDays < order.getRentalDays()) {
            refundAmount = order.getTotalAmount().subtract(actualAmount);
        }

        // 4. 更新订单
        order.setActualEndDate(now);
        order.setReturnPerson(returnPerson);
        order.setReturnPhone(returnPhone);
        order.setStatus(Constants.ORDER_STATUS_COMPLETED);
        order.setRefundAmount(refundAmount);

        this.updateById(order);

        // 5. 更新无人机状态为空闲
        droneService.updateDroneStatus(order.getDroneId(), Constants.DRONE_STATUS_IDLE);

        log.info("订单归还成功: orderId={}, 实际天数={}, 退款金额={}",
                orderId, actualDays, refundAmount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, String cancelReason) {
        log.info("取消订单: orderId={}, reason={}", orderId, cancelReason);

        // 1. 查询订单
        Orders order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 2. 验证订单状态
        if (!Constants.ORDER_STATUS_PENDING.equals(order.getStatus())) {
            throw new BusinessException("只有待支付的订单才能取消");
        }

        // 3. 更新订单状态
        order.setStatus(Constants.ORDER_STATUS_CANCELLED);
        order.setCancelReason(cancelReason);

        this.updateById(order);

        // 4. 更新无人机状态为空闲
        droneService.updateDroneStatus(order.getDroneId(), Constants.DRONE_STATUS_IDLE);

        log.info("订单取消成功: orderId={}", orderId);
    }

    /**
     * 生成订单编号
     * 格式：ORD + yyyyMMddHHmmss + 随机数
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().toString()
                .replace("-", "")
                .replace("T", "")
                .replace(":", "")
                .substring(0, 14);
        int random = (int) (Math.random() * 9000) + 1000;
        return "ORD" + timestamp + random;
    }
}
