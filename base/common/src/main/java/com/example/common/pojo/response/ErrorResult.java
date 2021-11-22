package com.example.common.pojo.response;

import lombok.Data;

@Data
public class ErrorResult<T> extends CommonResult<T>{

    /**
     * 异常的具体类名称
     */
    private String exceptionClazz;

    ErrorResult(String message) {
        super(false, DEFAULT_ERROR_CODE, message, null);
    }

    public ErrorResult(Integer code, String message) {
        super(false, code, message, null);
    }

    ErrorResult(Integer code, String message, T t) {
        super(false, code, message, t);
    }

}
