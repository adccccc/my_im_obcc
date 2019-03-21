package com.sysu.obcc.tcp.utils;

/**
 * @Author: obc
 * @Date: 2019/3/4 22:53
 * @Version 1.0
 */

import com.sysu.obcc.http.po.OffLineMessage;
import com.sysu.obcc.tcp.dto.MessageTask;
import com.sysu.obcc.tcp.proto.CcPacket;

import java.util.Date;

/**
 * 报文生成
 */
public class PacketUtils {

    /**
     * 生成通用Ack报文
     * @param messageId
     * @param content
     * @return
     */
    public static CcPacket.AckPacket generateAck(String messageId, String content) {
        CcPacket.AckPacket.Builder builder = CcPacket.AckPacket.newBuilder();
        builder.setAckId(messageId)
                .setTimestamp(new Date().getTime())
                .setContent("");
        return builder.build();
    }

    /**
     * 生成好友消息转发报文
     * @param packet
     * @return
     */
    public static CcPacket.SingleChatPacket generateSingleChat(CcPacket.SingleChatPacket packet) {
        CcPacket.SingleChatPacket.Builder builder = CcPacket.SingleChatPacket.newBuilder();
        builder.setMessageId(MsgIdUtils.getUUID())
                .setVersion(packet.getVersion())
                .setTimestamp(new Date().getTime())
                .setSenderId(packet.getSenderId())
                .setReceiverId(packet.getReceiverId())
                .setType(packet.getType())
                .setContent(packet.getContent());
        return builder.build();
    }

    public static CcPacket.SingleChatPacket generateSingleChat(OffLineMessage message) {
        CcPacket.SingleChatPacket.Builder builder = CcPacket.SingleChatPacket.newBuilder();
        builder.setMessageId(message.getMessageId())
                .setVersion(message.getVersion())
                .setTimestamp(message.getTimestamp())
                .setSenderId(message.getSenderId())
                .setReceiverId(message.getReceiverId())
                .setType(CcPacket.SingleChatPacket.MessageType.forNumber(message.getType()))
                .setContent(message.getContent());
        return builder.build();
    }

    /**
     * 生成认证失败报文
     * @param packet
     * @return
     */
    public static CcPacket.AuthErrorPacket generateAuthErrorPacket(CcPacket.AuthPacket packet) {
        return CcPacket.AuthErrorPacket.newBuilder()
                .setAckId(packet.getMessageId())
                .setTimestamp(new Date().getTime())
                .setContent("Token校验失败，请重新登录！")
                .build();
    }
}
