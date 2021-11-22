package com.example.common.consts;

/**
 * @author 10244
 */
public interface ExpEnumConstant {

    /**
     * 模块分类编码（2位）
     * <p>
     */
    int CORE_MODULE_EXP_CODE = 10;

    /* 分类编码（4位） */
    /**
     * 认证异常枚举
     */
    int AUTH_EXCEPTION_ENUM = 1100;

    /**
     * 参数校验异常枚举
     */
    int PARAM_EXCEPTION_ENUM = 1200;

    /**
     * 授权和鉴权异常的枚举
     */
    int PERMISSION_EXCEPTION_ENUM = 1300;

    /**
     * 请求方法相关异常枚举
     */
    int REQUEST_METHOD_EXCEPTION_ENUM = 1400;

    /**
     * 请求类型相关异常枚举
     */
    int REQUEST_TYPE_EXCEPTION_ENUM = 1500;

    /**
     * 服务器内部相关异常枚举
     */
    int SERVER_EXCEPTION_ENUM = 1600;

    /**
     * 状态相关异常枚举
     */
    int STATUS_EXCEPTION_ENUM = 1700;

    /**
     * 包装相关异常枚举
     */
    int WRAPPER_EXCEPTION_ENUM = 1800;

    /**
     * 系统用户相关异常枚举
     */
    int SYS_USER_EXCEPTION_ENUM = 1900;
}
