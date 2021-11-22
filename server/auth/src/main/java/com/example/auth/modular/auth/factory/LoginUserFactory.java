package com.example.auth.modular.auth.factory;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.example.auth.modular.user.service.ISysUserService;
import com.example.common.exception.ServiceException;
import com.example.common.exception.enums.ServerExceptionEnum;
import com.example.common.login.SysLoginUser;
import com.example.core.utils.HttpServletUtil;
import com.example.core.utils.IpAddressUtil;
import com.example.core.utils.UaUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author demo
 * @description
 */
public class LoginUserFactory {

    private static final ISysUserService sysUserService = SpringUtil.getBean(ISysUserService.class);

    public static void fillLoginUserInfo(SysLoginUser loginUser) {
        HttpServletRequest request = HttpServletUtil.getRequest();
        if (ObjectUtil.isNotEmpty(request)) {
            //填充基础信息
            loginUser.setLastLoginIp(IpAddressUtil.getIp(request));
            loginUser.setLastLoginTime(DateTime.now().toString());
            loginUser.setLastLoginAddress(IpAddressUtil.getAddress(request));
            loginUser.setLastLoginBrowser(UaUtil.getBrowser(request));
            loginUser.setLastLoginOs(UaUtil.getOs(request));
            Long userId = loginUser.getId();

            // todo 填充员工信息
            //填充员工信息
            //填充角色信息

        } else {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        }
    }

}
