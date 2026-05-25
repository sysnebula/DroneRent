package com.xxq.dronerent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxq.dronerent.entity.SysUser;
import com.xxq.dronerent.mapper.SysUserMapper;
import com.xxq.dronerent.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 系统用户 Service 实现类
 *
 * @author xxq
 * @since 2024-01-01
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
