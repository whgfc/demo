package com.example.auth.modular.auth.service;

import com.example.auth.modular.auth.entity.param.LoginParam;
import com.example.auth.modular.user.entity.po.SysUser;
import com.example.common.login.SysLoginUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录处理类
 */
public interface IAuthService {

    /**
     * 登录逻辑处理
     * @param account
     * @param password
     * @return
     */
    String doLogin(String account, String password);

    /**
     * 登录逻辑处理
     * @param sysUser
     * @return
     */
    String doLogin(SysUser sysUser);

    /**
     * 构造登录用户信息
     * @param sysUser
     * @return
     */
    SysLoginUser genSysLoginUser(SysUser sysUser);

    /**
     * 登出逻辑处理
     */
    void logout();

    /**
     * request中获取token
     * @param request
     * @return
     */
    String getTokenFromRequest(HttpServletRequest request);

    /**
     * 校验token是否正常
     * @param token
     */
    void checkToken(String token);
}
