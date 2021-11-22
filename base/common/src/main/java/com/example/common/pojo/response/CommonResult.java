package com.example.common.pojo.response;

import lombok.Data;

/**
 * 通用返回对象
 */
@Data
public class CommonResult<T> {

    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";

    public static final String DEFAULT_ERROR_MESSAGE = "网络异常";

    public static final Integer DEFAULT_SUCCESS_CODE = 200;

    public static final Integer DEFAULT_ERROR_CODE = 500;

    private Boolean success;
    private Integer code;
    private String msg;
    private T data;

    public CommonResult() {
    }

    public CommonResult(Boolean success, Integer code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static SuccessResult<String> success() {
        return new SuccessResult<>();
    }

    public static SuccessResult<Object> success(Object obj) {
        return new SuccessResult<>(obj);
    }

    public static SuccessResult<Object> success(Integer code, String message, Object object) {
        return new SuccessResult<>(code, message, object);
    }

    public static ErrorResult<Object> error(String message) {
        return new ErrorResult<>(message);
    }

    public static ErrorResult<Object> error(Integer code, String message) {
        return new ErrorResult<>(code, message);
    }

    public static ErrorResult<Object> error(Integer code, String message, Object object) {
        return new ErrorResult<>(code, message, object);
    }
}
