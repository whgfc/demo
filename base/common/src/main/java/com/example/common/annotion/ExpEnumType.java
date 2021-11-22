package com.example.common.annotion;

import java.lang.annotation.*;

/**
 * @author 10244
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExpEnumType {

    /**
     * 模块编码
     */
    int module() default 99;

    /**
     * 分类编码
     */
    int kind() default 9999;
}
