package com.sysu.obcc.tcp.utils;

/**
 * @Author: obc
 * @Date: 2019/3/4 22:53
 * @Version 1.0
 */

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
                .setContent(content);
        return builder.build();
    }

    /**
     * 生成好友消息转发报文
     * @param packet
     * @return
     */
    public static CcPacket.SingleChatPacket generateSingleChat(CcPacket.SingleChatPacket packet) {
        CcPacket.SingleChatPacket.Builder builder = CcPacket.SingleChatPacket.newBuilder();
        builder.setMessageId(packet.getMessageId())
                .setVersion(packet.getVersion())
                .setTimestamp(new Date().getTime())
                .setSenderId(packet.getSenderId())
                .setReceiverId(packet.getReceiverId())
                .setType(packet.getType())
                .setContent(packet.getContent());
        return builder.build();
    }
}
