package com.example.common.exception;

import com.example.common.exception.enums.abs.AbstractBaseExceptionEnum;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final Integer code;
    private final String errorMessage;

    public AuthException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }
}
