package com.sysu.obcc.tcp.dto;

/**
 * @Author: obc
 * @Date: 2019/3/4 23:16
 * @Version 1.0
 */

import com.google.protobuf.MessageLite;
import com.sysu.obcc.tcp.proto.PacketType;
import io.netty.channel.Channel;


/**
 * 转发消息使用的包装类
 */
public class MessageTask {

    private MessageLite messageLite;

    // 消息id，用于超时重传响应ack
    private String messageId;

    // 接收方channel，当接收方离线时channel为null
    private Channel channel;

    // 接收方id
    private String receiverId;

    // messageLite的消息类型
    private PacketType type;

    public MessageTask() {
    }

    public MessageTask(MessageLite messageLite, String messageId, Channel channel, String receiverId, PacketType type) {
        this.messageLite = messageLite;
        this.messageId = messageId;
        this.channel = channel;
        this.receiverId = receiverId;
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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return messageLite.toString();
    }
}
