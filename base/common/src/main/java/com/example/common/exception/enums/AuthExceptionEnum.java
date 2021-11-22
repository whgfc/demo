package com.example.common.exception.enums;

import com.example.common.annotion.ExpEnumType;
import com.example.common.consts.ExpEnumConstant;
import com.example.common.exception.enums.abs.AbstractBaseExceptionEnum;
import com.example.common.factory.ExpEnumCodeFactory;
import jdk.nashorn.internal.objects.annotations.Getter;

/**
 * @author 10244
 */
@ExpEnumType(module = ExpEnumConstant.CORE_MODULE_EXP_CODE, kind = ExpEnumConstant.AUTH_EXCEPTION_ENUM)
public enum AuthExceptionEnum implements AbstractBaseExceptionEnum {
    /**
     * 账号或密码为空
     */
    ACCOUNT_PWD_EMPTY(1, "账号或密码为空，请检查account或password参数"),

    /**
     * 账号密码错误
     */
    ACCOUNT_PWD_ERROR(2, "账号或密码错误，请检查account或password参数"),

    /**
     * 验证码错误
     */
    VALID_CODE_ERROR(3, "验证码错误，请检查captcha参数"),

    /**
     * 请求token为空
     */
    REQUEST_TOKEN_EMPTY(4, "请求token为空，请携带token访问本接口"),

    /**
     * token格式不正确，token请以Bearer开头
     */
    NOT_VALID_TOKEN_TYPE(5, "token格式不正确，token请以Bearer开头，并且Bearer后边带一个空格"),

    /**
     * 请求token错误
     */
    REQUEST_TOKEN_ERROR(6, "请求token错误"),

    /**
     * 账号被冻结
     */
    ACCOUNT_FREEZE_ERROR(7, "账号被冻结，请联系管理员"),

    /**
     * 登录已过期
     */
    LOGIN_EXPIRED(8, "登录已过期，请重新登录"),

    /**
     * 无登录用户
     */
    NO_LOGIN_USER(9, "无登录用户"),

    /**
     * 验证码错误
     */
    CONSTANT_EMPTY_ERROR(10, "验证码错误");

    private final Integer code;

    private final String message;

    AuthExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return ExpEnumCodeFactory.getExpEnumCode(this.getClass(), code);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
