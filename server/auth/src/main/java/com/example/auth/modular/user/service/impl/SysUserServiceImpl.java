package com.example.auth.modular.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.modular.user.entity.po.SysUser;
import com.example.auth.modular.user.entity.vo.SysUserParam;
import com.example.auth.modular.user.mapper.SysUserMapper;
import com.example.auth.modular.user.menus.SysUserExceptionEnum;
import com.example.auth.modular.user.service.ISysUserService;
import com.example.common.enums.CommonStatusEnum;
import com.example.common.exception.ServiceException;
import org.springframework.stereotype.Service;

/**
 * @author demo
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public SysUser getByAccount(String account) {
        return this.getOne( Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getAccount, account)
                .ne(SysUser::getStatus, CommonStatusEnum.DELETED.getCode()));
    }

    @Override
    public void add(SysUserParam sysUserParam) {
        checkParam(sysUserParam, false);

    }

    private void checkParam(SysUserParam sysUserParam, boolean isExcludeSelf) {
        Long id = sysUserParam.getId();
        String account = sysUserParam.getAccount();
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getAccount, account)
                .ne(SysUser::getStatus, CommonStatusEnum.DELETED.getCode());
        if (isExcludeSelf) {
            queryWrapper.eq(SysUser::getId, id);
        }
        int count = this.count(queryWrapper);
        //大于等于1个则表示重复
        if (count >= 1) {
            throw new ServiceException(SysUserExceptionEnum.USER_ACCOUNT_REPEAT);
        }
    }

}
