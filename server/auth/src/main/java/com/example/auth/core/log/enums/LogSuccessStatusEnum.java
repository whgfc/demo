package com.example.auth.core.log.enums;

import lombok.Getter;

/**
 * @author demo
 */
@Getter
public enum LogSuccessStatusEnum {
    /**
     * 失败
     */
    FAIL("N", "失败"),

    /**
     * 成功
     */
    SUCCESS("Y", "成功");

    private final String code;

    private final String message;

    LogSuccessStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
