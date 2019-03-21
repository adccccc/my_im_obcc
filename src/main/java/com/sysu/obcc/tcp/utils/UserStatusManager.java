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
    // 保存channel --> userId 的映射，用于某些情况下需要根据channel获取userId
    private static final Map<Channel, String> channelUser = new ConcurrentHashMap<>();

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
        // 如果有已连接的channel，关闭原channel(多设备登录逻辑)
        Channel lastChannel = userChannel.get(userId);
        if (lastChannel != null && lastChannel.isOpen()) {
            lastChannel.close();
        }

        userChannel.put(userId, channel);
        channelUser.put(channel, userId);
    }

    /**
     * 标记用户离线
     * @param userId
     */
    public void setOffline(String userId) {
        if (userId == null) return;
        Channel channel = userChannel.remove(userId);
        if (channel == null) return;
        channelUser.remove(channel);
    }

    /**
     * 标记Channel离线
     * @param channel
     */
    public void setOffline(Channel channel) {
        if (channel == null) return;
        String userId = channelUser.remove(channel);
        if (userId == null) return;
        // 这里需要判断channel是被挤下线还是主动离线
        if (userChannel.get(userId).equals(channel)) {
            userChannel.remove(userId);
        }
    }

    /**
     * 查询用户是否在线
     * @param userId
     * @return
     */
    public boolean isOnline(String userId) {
        if (userId == null) return false;
        return userChannel.containsKey(userId);
    }

    /**
     * 通过userId获取对应Channel
     * 不在线则返回null
     * @param userId
     * @return
     */
    public Channel getChannelByUserId(String userId) {
        if (userId == null) return null;
        return userChannel.get(userId);
    }

    /**
     * 通过channel获取对应userId
     * 不存在则返回null
     * @param channel
     * @return
     */
    public String getUserIdByChannel(Channel channel) {
        if (channel == null) return null;
        return channelUser.get(channel);
    }

    /**
     * 获取当前在线总人数
     * @return
     */
    public int getCountOfOnlineUser() {
        return userChannel.size();
    }
}
