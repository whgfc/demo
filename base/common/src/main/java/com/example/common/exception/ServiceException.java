package com.example.common.exception;

import com.example.common.exception.enums.abs.AbstractBaseExceptionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {

    private final Integer code;
    private final String errorMessage;

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }
}
