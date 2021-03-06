package com.example.common.context.constant;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author demo
 * @description 系统参数配置容器
 */
public class ConstantContext {

    private static final Dict CONSTANTS_HOLDER = Dict.create();

    /**
     * 添加系统常量
     */
    public static void putConstant(String code, Object value) {
        if (ObjectUtil.hasEmpty(code, value)) {
            return;
        }
        CONSTANTS_HOLDER.put(code, value);
    }

    /**
     * 删除常量，系统常量无法删除，在sysConfig已判断
     */
    public static void deleteConstant(String code) {
        if (ObjectUtil.hasEmpty(code)) {
            return;
        }
        CONSTANTS_HOLDER.remove(code);
    }

    /**
     * 获取系统常量本身
     */
    public static Dict me() {
        return CONSTANTS_HOLDER;
    }

}
