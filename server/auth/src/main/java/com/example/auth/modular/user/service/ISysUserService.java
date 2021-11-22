package com.example.auth.modular.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auth.modular.user.entity.po.SysUser;
import com.example.auth.modular.user.entity.vo.SysUserParam;

/**
 * @author demo
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据用户账户获取账户信息
     * @param account
     * @return
     */
    SysUser getByAccount(String account);

    /**
     * 新增账户
     * @param sysUserParam
     */
    void add(SysUserParam sysUserParam);


}
