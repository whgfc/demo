package com.example.common.exception;

import com.example.common.exception.enums.abs.AbstractBaseExceptionEnum;
import lombok.Getter;

@Getter
public class RequestMethodException extends RuntimeException {

    private final Integer code;

    private final String errorMessage;

    public RequestMethodException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

}
