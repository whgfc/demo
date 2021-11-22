package com.example.common.context.requestNo;

/**
 * @author demo
 * @description
 */
public class RequestNoContext {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 保存请求号
     */
    public static void set(String requestNo) {
        CONTEXT_HOLDER.set(requestNo);
    }

    /**
     * 获取请求号
     */
    public static String get() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除请求号
     */
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
