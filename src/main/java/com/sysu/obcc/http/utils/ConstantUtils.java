package com.sysu.obcc.http.utils;

/**
 * @Author: obc
 * @Date: 2019/2/22 22:38
 * @Version 1.0
 */

/**
 * 存储常数
 */
public final class ConstantUtils {

    // 默认redis缓存过期时间(s)
    public static final int DEFAULT_REDIS_EXPIRE = 3600;
    // 登录token过期时间(s)
    public static final long TOKEN_EXPIRE = 60*60*24;



    public static final String TOKEN_PREFIX = "token_";
}
