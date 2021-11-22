package com.example.common.exception.enums;

import com.example.common.annotion.ExpEnumType;
import com.example.common.consts.ExpEnumConstant;
import com.example.common.exception.enums.abs.AbstractBaseExceptionEnum;
import com.example.common.factory.ExpEnumCodeFactory;

@ExpEnumType(module = ExpEnumConstant.CORE_MODULE_EXP_CODE, kind = ExpEnumConstant.STATUS_EXCEPTION_ENUM)
public enum StatusExceptionEnum implements AbstractBaseExceptionEnum {
    /**
     * 请求状态值为空
     */
    REQUEST_EMPTY(1, "请求状态值为空"),

    /**
     * 请求状值为非正确状态值
     */
    NOT_WRITE_STATUS(2, "请求状态值不合法"),

    /**
     * 更新状态失败，试图更新被删除的记录
     */
    UPDATE_STATUS_ERROR(3, "更新状态失败，您试图更新被删除的记录");

    private final Integer code;

    private final String message;

    StatusExceptionEnum(Integer code, String message) {
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
