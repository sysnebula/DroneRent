package com.xxq.dronerent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxq.dronerent.entity.Customer;
import com.xxq.dronerent.mapper.CustomerMapper;
import com.xxq.dronerent.service.CustomerService;
import org.springframework.stereotype.Service;

/**
 * 客户 Service 实现类
 *
 * @author xxq
 * @since 2024-01-01
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
