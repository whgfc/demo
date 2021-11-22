package com.example.common.pojo.response;

import lombok.Data;

@Data
public class SuccessResult<T> extends CommonResult<T> {

    public SuccessResult() {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public SuccessResult(T t) {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, t);
    }

    public SuccessResult(Integer code, String message, T t) {
        super(true, code, message, t);
    }
}
