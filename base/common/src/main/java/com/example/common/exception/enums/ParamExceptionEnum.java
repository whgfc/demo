package com.example.common.exception.enums;

import com.example.common.annotion.ExpEnumType;
import com.example.common.consts.ExpEnumConstant;
import com.example.common.exception.enums.abs.AbstractBaseExceptionEnum;
import com.example.common.factory.ExpEnumCodeFactory;

@ExpEnumType(module = ExpEnumConstant.CORE_MODULE_EXP_CODE, kind = ExpEnumConstant.PARAM_EXCEPTION_ENUM)
public enum ParamExceptionEnum implements AbstractBaseExceptionEnum {
    /**
     * 参数错误
     */
    PARAM_ERROR(1, "参数错误");

    private final Integer code;
    private final String message;

    ParamExceptionEnum(Integer code, String message) {
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
