package com.xxq.dronerent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxq.dronerent.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户 Mapper 接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
