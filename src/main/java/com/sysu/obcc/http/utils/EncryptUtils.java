package com.sysu.obcc.http.utils;

/**
 * @Author: obc
 * @Date: 2019/2/22 23:43
 * @Version 1.0
 */

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密工具类
 */
public final class EncryptUtils {

    /**
     * 随机生成32位Hex盐值
     * @return
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return byteToHex(bytes);
    }

    /**
     * 使用SHA256加密
     * @param password
     * @param salt
     * @return 64位Hex字符串
     */
    public static String encryptToSHA256(String password, String salt) {
        MessageDigest messageDigest;
        String encdeStr = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            String encryptStr = password + salt;
            byte[] hash = messageDigest.digest(encryptStr.getBytes("UTF-8"));
            encdeStr = byteToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    /**
     * byte转Hex字符串
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {   // 一位则补0
                builder.append('0');
            }
            builder.append(temp);
        }
        return builder.toString();
    }
}
