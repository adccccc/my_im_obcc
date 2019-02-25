package com.sysu.obcc.http.utils;

/**
 * @Author: obc
 * @Date: 2019/2/23 21:53
 * @Version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Token管理
 */
@Component
public class TokenUtils {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 生成token，过期时间为24小时
     * @param username
     * @return
     */
    public String generateToken(String username) {
        if (username == null) return null;

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = EncryptUtils.byteToHex(bytes);

        return redisUtils.set(ConstantUtils.TOKEN_PREFIX + username, token, ConstantUtils.TOKEN_EXPIRE) ? token : null;
    }

    public String getToken(String username) {
        if (username == null) return null;
        Object token = redisUtils.get(ConstantUtils.TOKEN_PREFIX + username);

        return token == null ? null : token.toString();
    }

    /**
     * token验证
     *
     * @param username
     * @param token
     * @return
     */
    public boolean vertifyToken(String username, String token) {
        if (username == null || token == null) return false;
        String token1 = redisUtils.get(ConstantUtils.TOKEN_PREFIX + username).toString();
        if (!token1.equals(token)) {
            redisUtils.deleteKeys(ConstantUtils.TOKEN_PREFIX + username);
            return false;
        } else {
            // 刷新过期时间
            redisUtils.set(ConstantUtils.TOKEN_PREFIX + username, token, ConstantUtils.TOKEN_EXPIRE);
            return true;
        }
    }

    /**
     * 使登录用户的token失效
     * @param username
     */
    public void expireToken(String username) {
        if (username != null) redisUtils.deleteKeys(ConstantUtils.TOKEN_PREFIX + username);

    }
}
