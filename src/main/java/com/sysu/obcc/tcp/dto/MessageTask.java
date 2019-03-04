package com.sysu.obcc.tcp.dto;

/**
 * @Author: obc
 * @Date: 2019/3/4 23:16
 * @Version 1.0
 */

import com.google.protobuf.MessageLite;
import com.sysu.obcc.tcp.proto.PacketType;

import java.nio.channels.Channel;

/**
 * 转发消息使用的包装类
 */
public class MessageTask {

    private MessageLite messageLite;

    private Channel channel;

    private PacketType type;

    public MessageTask() {
    }

    public MessageTask(MessageLite messageLite, Channel channel, PacketType type) {
        this.messageLite = messageLite;
        this.channel = channel;
        this.type = type;
    }

    public MessageLite getMessageLite() {
        return messageLite;
    }

    public void setMessageLite(MessageLite messageLite) {
        this.messageLite = messageLite;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }
}
