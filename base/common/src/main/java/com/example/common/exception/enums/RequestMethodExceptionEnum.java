package com.example.common.exception.enums;

import com.example.common.annotion.ExpEnumType;
import com.example.common.consts.ExpEnumConstant;
import com.example.common.exception.RequestMethodException;
import com.example.common.exception.enums.abs.AbstractBaseExceptionEnum;
import com.example.common.factory.ExpEnumCodeFactory;
import lombok.Getter;

@ExpEnumType(module = ExpEnumConstant.CORE_MODULE_EXP_CODE, kind = ExpEnumConstant.REQUEST_METHOD_EXCEPTION_ENUM)
public enum RequestMethodExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 不支持该请求方法，请求方法应为POST
     */
    REQUEST_METHOD_IS_POST(1, "不支持该请求方法，请求方法应为POST"),

    /**
     * 不支持该请求方法，请求方法应为GET
     */
    REQUEST_METHOD_IS_GET(2, "不支持该请求方法，请求方法应为GET");

    private final Integer code;
    private final String message;

    RequestMethodExceptionEnum(Integer code, String message) {
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
