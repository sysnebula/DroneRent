package com.xxq.dronerent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxq.dronerent.entity.FinanceTransaction;
import org.apache.ibatis.annotations.Mapper;

/**
 * 财务流水 Mapper 接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Mapper
public interface FinanceTransactionMapper extends BaseMapper<FinanceTransaction> {

}
