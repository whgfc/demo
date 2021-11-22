package com.example.common.context.constant;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.consts.CommonConstant;
import com.example.common.consts.SymbolConstant;
import com.example.common.exception.ServiceException;
import com.example.common.exception.enums.ServerExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author demo
 * @description 系统参数配置获取
 */
public class ConstantContextHolder<T> {

    private static final Logger logger = LoggerFactory.getLogger(ConstantContextHolder.class);

    /**
     * 获取放开xss过滤的接口
     * @return
     */
    public static List<String> getUnXssFilterUrl() {
        String snowyUnXssFilterUrl = getSysConfigWithDefault("UN_XSS_FILTER_URL", String.class, null);
        if (ObjectUtil.isEmpty(snowyUnXssFilterUrl)) {
            return CollectionUtil.newArrayList();
        } else {
            return CollectionUtil.toList(snowyUnXssFilterUrl.split(SymbolConstant.COMMA));
        }
    }

    /**
     * 获取jwt秘钥
     * @return
     */
    public static String getJwtSecret() {
        return getSysConfigWithDefault("JWT_SECRET", String.class, CommonConstant.DEFAULT_JWT_PASSWORD);
    }

    /**
     * 获取默认密码
     * @return
     */
    public static String defaultPassword() {
        return getSysConfigWithDefault("DEFAULT_PASSWORD", String.class, CommonConstant.DEFAULT_PASSWORD);
    }

    /**
     * 获取session失效时间
     * @return
     */
    public static Long getSessionExpireSec() {
        return getSysConfigWithDefault("SESSION_EXPIRE", Long.class, 30*60L);
    }

    /**
     * 获取token失效时间
     * @return
     */
    public static Long getTokenExpireSec() {
        return getSysConfigWithDefault("TOKEN_EXPIRE", Long.class, 7*24*60*60L);
    }

    /**
     * 获取config表中的配置，如果为空返回默认值
     *
     * @param configCode   变量名称，对应sys_config表中的code
     * @param clazz        返回变量值的类型
     * @param defaultValue 如果结果为空返回此默认值
     */
    public static <T> T getSysConfigWithDefault(String configCode, Class<T> clazz, T defaultValue) {
        String configValue = ConstantContext.me().getStr(configCode);
        if (ObjectUtil.isEmpty(configValue)) {
            // 将默认值加入到缓存常量
            logger.warn(">>> 系统配置sys_config表中存在空项，configCode为：{}，系统采用默认值：{}", configCode, defaultValue);
            ConstantContext.me().put(configCode, defaultValue);
            return defaultValue;
        } else {
            try {
                return Convert.convert(clazz, configValue);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    /**
     * 获取config表中的配置，如果为空，是否抛出异常
     *
     * @param configCode   变量名称，对应sys_config表中的code
     * @param clazz        返回变量值的类型
     * @param nullThrowExp 若为空是否抛出异常
     * @author yubaoshan
     * @date 2020/6/20 22:03
     */
    public static <T> T getSysConfig(String configCode, Class<T> clazz, Boolean nullThrowExp) {
        String configValue = ConstantContext.me().getStr(configCode);
        if (ObjectUtil.isEmpty(configValue)) {
            if (nullThrowExp) {
                String format = StrUtil.format(">>> 系统配置sys_config表中存在空项，configCode为：{}", configCode);
                logger.error(format);
                throw new ServiceException(ServerExceptionEnum.CONSTANT_EMPTY.getCode(), format);
            } else {
                return null;
            }
        } else {
            try {
                return Convert.convert(clazz, configValue);
            } catch (Exception e) {
                if (nullThrowExp) {
                    String format = StrUtil.format(">>> 系统配置sys_config表中存在格式错误的值，configCode={}，configValue={}", configCode, configValue);
                    logger.error(format);
                    throw new ServiceException(ServerExceptionEnum.CONSTANT_EMPTY.getCode(), format);
                } else {
                    return null;
                }
            }
        }
    }

}
