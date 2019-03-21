package com.sysu.obcc.http.utils;

/**
 * @Author: obc
 * @Date: 2019/2/23 21:53
 * @Version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;

/**
 * Token管理
 */
@Component
public class TokenUtils {

    public static TokenUtils tokenUtils;

    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void init() {
        tokenUtils = this;
        tokenUtils.redisUtils = this.redisUtils;
    }


    /**
     * 生成token，过期时间为24小时
     * @param username
     * @return
     */
    public static String generateToken(String username) {
        if (username == null) return null;

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = EncryptUtils.byteToHex(bytes);

        return tokenUtils.redisUtils.set(ConstantUtils.TOKEN_PREFIX + username, token, ConstantUtils.TOKEN_EXPIRE) ? token : null;
    }

    public static String getToken(String username) {
        if (username == null) return null;
        Object token = tokenUtils.redisUtils.get(ConstantUtils.TOKEN_PREFIX + username);

        return token == null ? null : token.toString();
    }

    /**
     * token验证
     *
     * @param username
     * @param token
     * @return
     */
    public static boolean verifyToken(String username, String token) {

        if (username == null || token == null) return false;
        String token1 = (String)tokenUtils.redisUtils.get(ConstantUtils.TOKEN_PREFIX + username);
        if (token1 == null || !token1.equals(token)) {
            // tokenUtils.redisUtils.deleteKeys(ConstantUtils.TOKEN_PREFIX + username);
            return false;
        } else {
            // 刷新过期时间
            tokenUtils.redisUtils.set(ConstantUtils.TOKEN_PREFIX + username, token, ConstantUtils.TOKEN_EXPIRE);
            return true;
        }
    }

    /**
     * 使登录用户的token失效
     * @param username
     */
    public static void expireToken(String username) {
        if (username != null) tokenUtils.redisUtils.deleteKeys(ConstantUtils.TOKEN_PREFIX + username);

    }
}
