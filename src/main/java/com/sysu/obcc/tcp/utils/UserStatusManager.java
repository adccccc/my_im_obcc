package com.sysu.obcc.tcp.utils;

/**
 * @Author: obc
 * @Date: 2019/3/3 12:48
 * @Version 1.0
 */

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户在线状态管理
 * 保存userId --> channel映射
 * 单例
 */
public class UserStatusManager {

    // 保存userId --> channel 的映射，用于查询是否在线并获取对应channel
    private static final Map<String, Channel> userChannel = new ConcurrentHashMap<>();

    private static volatile UserStatusManager instance = null;

    private UserStatusManager() {}

    public static UserStatusManager getInstance() {
        if (instance == null) {
            synchronized (UserStatusManager.class) {
                if (instance == null) {
                    instance = new UserStatusManager();
                }
            }
        }
        return instance;
    }

    /**
     * 标记用户为在线
     * @param userId
     * @param channel
     */
    public void setOnline(String userId, Channel channel) {
        userChannel.put(userId, channel);
    }

    /**
     * 标记用户离线
     * @param userId
     */
    public void setOffline(String userId) {
        userChannel.remove(userId);
    }

    /**
     * 查询用户是否在线
     * @param userId
     * @return
     */
    public boolean isOnline(String userId) {
        return userChannel.containsKey(userId);
    }

    /**
     * 通过userId获取对应Channel
     * 不在线则返回null
     * @param userId
     * @return
     */
    public Channel getChannelByUserId(String userId) {
        return userChannel.get(userId);
    }

    /**
     * 获取当前在线总人数
     * @return
     */
    public int getCountOfOnlineUser() {
        return userChannel.size();
    }
}
