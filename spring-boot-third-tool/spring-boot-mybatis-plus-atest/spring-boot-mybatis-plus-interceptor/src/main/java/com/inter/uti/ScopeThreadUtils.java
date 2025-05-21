package com.inter.uti;


public class ScopeThreadUtils {

    private static final ThreadLocal<String> SCOPE_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置 权限标识
     */
    public static void setDataScope() {
        SCOPE_THREAD_LOCAL.set(ScopeConstants.DATA_SCOPE);
    }

    /**
     * 获取 权限标识
     */
    public static String getDataScope() {
        return SCOPE_THREAD_LOCAL.get();
    }

    /**
     * 清除 权限标识
     */
    public static void clearDataScope() {
        SCOPE_THREAD_LOCAL.remove();
    }

}
