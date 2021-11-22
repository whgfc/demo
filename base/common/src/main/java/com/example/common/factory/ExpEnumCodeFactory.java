package com.example.common.factory;

import com.example.common.annotion.ExpEnumType;

public class ExpEnumCodeFactory {

    public static Integer getExpEnumCode(Class<?> clazz, int code) {
        // 默认的异常响应码
        Integer defaultCode = Integer.valueOf("" + 99 + 9999 + 9);
        if (null == clazz) {
            return defaultCode;
        }
        ExpEnumType annotation = clazz.getAnnotation(ExpEnumType.class);
        if (null == annotation) {
            return defaultCode;
        }
        return Integer.valueOf("" + annotation.module() + annotation.kind() + code);
    }

}
