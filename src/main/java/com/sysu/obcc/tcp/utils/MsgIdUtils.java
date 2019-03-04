package com.sysu.obcc.tcp.utils;

/**
 * @Author: obc
 * @Date: 2019/3/4 22:21
 * @Version 1.0
 */

import java.util.UUID;

/**
 * 消息id工具类
 */
public class MsgIdUtils {

    /**
     * 判断当前Msg是否为最新报文
     * 取msgID前8位进行比较
     * @param lastId
     * @param curId
     * @return
     */
    public static boolean isLatest(String lastId, String curId) {
        String id1 = lastId.substring(0, 8);
        String id2 = curId.substring(0, 8);
        return id1.compareTo(id2) > 0;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
