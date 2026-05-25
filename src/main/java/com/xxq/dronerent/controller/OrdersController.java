package com.xxq.dronerent.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxq.dronerent.common.BusinessException;
import com.xxq.dronerent.common.PageResult;
import com.xxq.dronerent.common.Result;
import com.xxq.dronerent.dto.OrderCreateDTO;
import com.xxq.dronerent.dto.OrderUpdateDTO;
import com.xxq.dronerent.entity.Orders;
import com.xxq.dronerent.security.SecurityUser;
import com.xxq.dronerent.service.OrdersService;
import com.xxq.dronerent.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理控制器
 * 提供订单的增删改查、续租、归还、取消等接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Slf4j
@Tag(name = "订单管理", description = "租赁订单的管理接口")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    /**
     * 分页查询订单列表
     *
     * @param pageNum    页码
     * @param pageSize   每页条数
     * @param status     订单状态（可选）
     * @param customerId 客户ID（可选）
     * @return 分页结果
     */
    @Operation(summary = "分页查询订单列表", description = "支持按状态和客户筛选")
    @GetMapping("/page")
    public Result<PageResult<OrderVO>> pageOrders(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "订单状态") @RequestParam(required = false) String status,
            @Parameter(description = "客户ID") @RequestParam(required = false) Long customerId) {

        log.info("分页查询订单: pageNum={}, pageSize={}, status={}, customerId={}",
                pageNum, pageSize, status, customerId);

        // 构建查询条件
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(status != null, Orders::getStatus, status)
               .eq(customerId != null, Orders::getCustomerId, customerId)
               .orderByDesc(Orders::getCreateTime);

        // 分页查询
        Page<Orders> page = new Page<>(pageNum, pageSize);
        Page<Orders> orderPage = ordersService.page(page, wrapper);

        // 转换为 VO
        List<OrderVO> orderVOList = orderPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageResult<OrderVO> result = PageResult.build(
                orderPage.getTotal(),
                orderPage.getCurrent(),
                orderPage.getSize(),
                orderVOList
        );

        return Result.success(result);
    }

    /**
     * 根据ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单信息
     */
    @Operation(summary = "查询订单详情", description = "根据订单ID查询详细信息")
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderById(
            @Parameter(description = "订单ID") @PathVariable Long id) {

        Orders order = ordersService.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        return Result.success(convertToVO(order));
    }

    /**
     * 创建订单
     *
     * @param orderCreateDTO 订单创建请求
     * @param authentication 认证对象（自动注入）
     * @return 订单ID
     */
    @Operation(summary = "创建订单", description = "创建新的租赁订单")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Result<Long> createOrder(
            @Valid @RequestBody OrderCreateDTO orderCreateDTO,
            Authentication authentication) {

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userId = securityUser.getUserId();

        log.info("创建订单: userId={}", userId);

        Long orderId = ordersService.createOrder(orderCreateDTO, userId);

        return Result.success("订单创建成功", orderId);
    }

    /**
     * 更新订单信息
     *
     * @param id             订单ID
     * @param orderUpdateDTO 订单更新请求
     * @return 操作结果
     */
    @Operation(summary = "更新订单信息", description = "修改订单备注等信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Result<Void> updateOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Valid @RequestBody OrderUpdateDTO orderUpdateDTO) {

        log.info("更新订单信息: id={}", id);

        Orders order = ordersService.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 更新字段
        if (orderUpdateDTO.getReturnPerson() != null) {
            order.setReturnPerson(orderUpdateDTO.getReturnPerson());
        }
        if (orderUpdateDTO.getReturnPhone() != null) {
            order.setReturnPhone(orderUpdateDTO.getReturnPhone());
        }
        if (orderUpdateDTO.getRemark() != null) {
            order.setRemark(orderUpdateDTO.getRemark());
        }

        ordersService.updateById(order);

        return Result.success("更新成功", null);
    }

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @Operation(summary = "删除订单", description = "根据ID删除订单（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {

        log.info("删除订单: id={}", id);

        Orders order = ordersService.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 只有已取消的订单才能删除
        if (!"CANCELLED".equals(order.getStatus())) {
            throw new BusinessException("只能删除已取消的订单");
        }

        // 删除（逻辑删除）
        ordersService.removeById(id);

        return Result.success("删除成功", null);
    }

    /**
     * 续租订单
     *
     * @param id         订单ID
     * @param newEndDate 新的结束日期
     * @return 操作结果
     */
    @Operation(summary = "续租订单", description = "延长订单的租赁结束日期")
    @PostMapping("/{id}/extend")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Result<Void> extendOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "新的结束日期") @RequestParam LocalDate newEndDate) {

        log.info("续租订单: id={}, newEndDate={}", id, newEndDate);

        ordersService.extendOrder(id, newEndDate);

        return Result.success("续租成功", null);
    }

    /**
     * 提前归还
     *
     * @param id              订单ID
     * @param returnPerson    还机人姓名
     * @param returnPhone     还机人电话
     * @return 操作结果
     */
    @Operation(summary = "提前归还", description = "提前归还无人机，完成订单")
    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Result<Void> earlyReturn(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "还机人姓名") @RequestParam(required = false) String returnPerson,
            @Parameter(description = "还机人电话") @RequestParam(required = false) String returnPhone) {

        log.info("提前归还: id={}", id);

        ordersService.earlyReturn(id, returnPerson, returnPhone);

        return Result.success("归还成功", null);
    }

    /**
     * 取消订单
     *
     * @param id           订单ID
     * @param cancelReason 取消原因
     * @return 操作结果
     */
    @Operation(summary = "取消订单", description = "取消待支付的订单")
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Result<Void> cancelOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "取消原因") @RequestParam String cancelReason) {

        log.info("取消订单: id={}, reason={}", id, cancelReason);

        ordersService.cancelOrder(id, cancelReason);

        return Result.success("订单已取消", null);
    }

    /**
     * 将 Orders 转换为 OrderVO
     */
    private OrderVO convertToVO(Orders order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        // TODO: 可以在此处填充客户姓名、无人机型号等关联信息
        return vo;
    }
}
