package com.xxq.dronerent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxq.dronerent.entity.FinanceTransaction;
import com.xxq.dronerent.mapper.FinanceTransactionMapper;
import com.xxq.dronerent.service.FinanceTransactionService;
import org.springframework.stereotype.Service;

/**
 * 财务流水 Service 实现类
 *
 * @author xxq
 * @since 2024-01-01
 */
@Service
public class FinanceTransactionServiceImpl extends ServiceImpl<FinanceTransactionMapper, FinanceTransaction> implements FinanceTransactionService {

}
