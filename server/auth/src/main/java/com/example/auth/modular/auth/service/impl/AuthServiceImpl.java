package com.example.auth.modular.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.example.auth.core.log.LogManager;
import com.example.auth.core.log.enums.LogSuccessStatusEnum;
import com.example.auth.modular.auth.factory.LoginUserFactory;
import com.example.auth.modular.auth.service.IAuthService;
import com.example.auth.modular.user.entity.po.SysUser;
import com.example.auth.modular.user.service.ISysUserService;
import com.example.cache.UserCache;
import com.example.common.consts.CommonConstant;
import com.example.common.consts.SymbolConstant;
import com.example.common.context.constant.ConstantContextHolder;
import com.example.common.enums.CommonStatusEnum;
import com.example.common.exception.AuthException;
import com.example.common.exception.ServiceException;
import com.example.common.exception.enums.AuthExceptionEnum;
import com.example.common.exception.enums.ServerExceptionEnum;
import com.example.common.login.SysLoginUser;
import com.example.core.jwt.JwtPayLoad;
import com.example.core.jwt.JwtTokenUtil;
import com.example.core.utils.HttpServletUtil;
import com.example.core.utils.IpAddressUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author demo
 */
@Service("authServiceImpl")
public class AuthServiceImpl implements IAuthService {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private UserCache userCache;

    /**
     * 账户登录逻辑处理
     * @param account
     * @param password
     * @return
     */
    @Override
    public String doLogin(String account, String password) {
        if (ObjectUtil.hasEmpty(account, password)) {
            LogManager.me().executeLoginLog(account, LogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_EMPTY.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_EMPTY);
        }

        SysUser sysUser = sysUserService.getByAccount(account);

        if (ObjectUtil.isNull(sysUser)) {
            LogManager.me().executeLoginLog(account, LogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_ERROR);
        }

        String dbPassword = sysUser.getPassword();
        if (ObjectUtil.isEmpty(dbPassword) || !BCrypt.checkpw(dbPassword, password)) {
            LogManager.me().executeLoginLog(account, LogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_ERROR);
        }
        return doLogin(sysUser);
    }

    @Override
    public String doLogin(SysUser sysUser) {
        Integer sysUserStatus = sysUser.getStatus();

        //验证账号是否被冻结
        if (CommonStatusEnum.DISABLE.getCode().equals(sysUserStatus)) {
            LogManager.me().executeLoginLog(sysUser.getAccount(), LogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_FREEZE_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_FREEZE_ERROR);
        }

        //构造SysLoginUser
        SysLoginUser sysLoginUser = this.genSysLoginUser(sysUser);

        //构造jwtPayLoad
        JwtPayLoad jwtPayLoad = new JwtPayLoad(sysUser.getId(), sysUser.getAccount());

        //生成token
        String token = JwtTokenUtil.generateToken(jwtPayLoad);

        //缓存token与登录用户信息对应, 默认2个小时
        this.cacheLoginUser(jwtPayLoad, sysLoginUser);

        //设置最后登录ip和时间
        sysUser.setLastLoginIp(IpAddressUtil.getIp(HttpServletUtil.getRequest()));
        sysUser.setLastLoginTime(DateTime.now());

        //更新用户登录信息
        sysUserService.updateById(sysUser);

        //登录成功，记录登录日志
        LogManager.me().executeLoginLog(sysUser.getAccount(), LogSuccessStatusEnum.SUCCESS.getCode(), null);

        //登录成功，设置SpringSecurityContext上下文，方便获取用户
        StpUtil.login(jwtPayLoad.getUuid());

        //如果开启限制单用户登陆，则踢掉原来的用户
        Boolean singleLogin = sysUser.getSingleLogin();
        if (singleLogin) {
            //获取所有的登陆用户
            Map<String, SysLoginUser> allLoginUsers = userCache.getAllKeyValues(jwtPayLoad.getUserId());
            for (Map.Entry<String, SysLoginUser> loginUserEntry : allLoginUsers.entrySet()) {

                String loginUserKey = loginUserEntry.getKey();
                SysLoginUser loginUser = loginUserEntry.getValue();

                //如果账号名称相同，并且redis缓存key和刚刚生成的用户的uuid不一样，则清除以前登录的
                if (loginUser.getName().equals(sysUser.getName())
                        && !loginUserKey.equals(jwtPayLoad.getUuid())) {
                    this.clearUser(jwtPayLoad.getUserId(), loginUserKey, loginUser.getAccount());
                }
            }
        }

        //返回token
        return token;
    }

    /**
     * 根据key清空登陆信息
     */
    private void clearUser(Long userId, String loginUserKey, String account) {
        //获取缓存的用户
        Object cacheObject = userCache.get(userId + SymbolConstant.COLON + loginUserKey);

        //如果缓存的用户存在，清除会话，否则表示该会话信息已失效，不执行任何操作
        if (ObjectUtil.isNotEmpty(cacheObject)) {
            //清除登录会话
            userCache.remove(loginUserKey);
            //创建退出登录日志
            LogManager.me().executeExitLog(account);
        }
    }

    /**
     * 构建登录用户信息
     * @param sysUser
     * @return
     */
    @Override
    public SysLoginUser genSysLoginUser(SysUser sysUser) {
        SysLoginUser sysLoginUser = new SysLoginUser();
        BeanUtil.copyProperties(sysUser, sysLoginUser);
        LoginUserFactory.fillLoginUserInfo(sysLoginUser);
        return sysLoginUser;
    }

    @Override
    public void logout() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        if (ObjectUtil.isNotNull(request)) {

            //获取token
            String token = this.getTokenFromRequest(request);

            //如果token为空直接返回
            if (ObjectUtil.isEmpty(token)) {
                return;
            }

            //校验token，错误则抛异常，待确定
            this.checkToken(token);

            //根据token获取JwtPayLoad部分
            JwtPayLoad jwtPayLoad = JwtTokenUtil.getJwtPayLoad(token);

            //获取缓存的key
            String loginUserCacheKey = jwtPayLoad.getUuid();
            this.clearUser(jwtPayLoad.getUserId(), loginUserCacheKey, jwtPayLoad.getAccount());

        } else {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        }
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        String authToken = request.getHeader(CommonConstant.AUTHORIZATION);
        if (ObjectUtil.isEmpty(authToken) || CommonConstant.UNDEFINED.equals(authToken)) {
            return null;
        } else {
            //token不是以Bearer打头，则响应回格式不正确
            if (!authToken.startsWith(CommonConstant.TOKEN_TYPE_BEARER)) {
                throw new AuthException(AuthExceptionEnum.NOT_VALID_TOKEN_TYPE);
            }
            try {
                authToken = authToken.substring(CommonConstant.TOKEN_TYPE_BEARER.length() + 1);
            } catch (StringIndexOutOfBoundsException e) {
                throw new AuthException(AuthExceptionEnum.NOT_VALID_TOKEN_TYPE);
            }
        }
        return authToken;
    }

    @Override
    public void checkToken(String token) {
        //校验token是否正确
        Boolean tokenCorrect = JwtTokenUtil.checkToken(token);
        if (!tokenCorrect) {
            throw new AuthException(AuthExceptionEnum.REQUEST_TOKEN_ERROR);
        }

        //校验token是否失效
        Boolean tokenExpired = JwtTokenUtil.isTokenExpired(token);
        if (tokenExpired) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPIRED);
        }
    }

    /**
     * 缓存token与登录用户信息对应, 默认30分钟
     */
    private void cacheLoginUser(JwtPayLoad jwtPayLoad, SysLoginUser sysLoginUser) {
        String key = jwtPayLoad.getUserId() + SymbolConstant.COLON + jwtPayLoad.getUserId();
        userCache.put(key, sysLoginUser, Convert.toLong(ConstantContextHolder.getSessionExpireSec()));
    }
}
