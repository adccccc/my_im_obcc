package com.sysu.obcc.tcp.proto;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: obc
 * @Date: 2019/3/2 17:16
 * @Version 1.0
 */

/**
 * 自定义protobuf编码器
 * 支持非指定message
 */
public class CustomProtobufEncoder extends MessageToByteEncoder<MessageLite> {

    /**
     * 编码
     * byte[] msg   -->    (byte[4] length) + (byte[1] type) + (byte[] msg)
     * todo: length的类型  int --> varInt32
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageLite msg, ByteBuf out) throws Exception {
        byte[] body = msg.toByteArray();
        int length = body.length;
        byte type = getType(msg);

        out.writeInt(length);
        out.writeByte(type);
        out.writeBytes(body);
    }

    /**
     * 枚举各报文类型
     * todo: 优化 if...else
     * @param msg
     * @return
     */
    private byte getType(MessageLite msg) {
        byte msgType = 0x0f;
        if (msg instanceof CcPacket.AuthPacket) {
            msgType = 0x01;
        } else if (msg instanceof CcPacket.HeartBeatPacket) {
            msgType = 0x02;
        } else if (msg instanceof CcPacket.SingleChatPacket) {
            msgType = 0x03;
        } else if (msg instanceof CcPacket.GroupChatPacket) {
            msgType = 0x04;
        } else if (msg instanceof CcPacket.FriendEventPacket) {
            msgType = 0x05;
        } else if (msg instanceof CcPacket.GroupEventPacket) {
            msgType = 0x06;
        } else if (msg instanceof CcPacket.AckPacket) {
            msgType = 0x07;
        } else if (msg instanceof CcPacket.AuthErrorPacket) {
            msgType = 0x08;
        } else if (msg instanceof CcPacket.InvalidChatPacket) {
            msgType = 0x09;
        } else if (msg instanceof CcPacket.InvalidEventPacket) {
            msgType = 0x0a;
        }
        return msgType;
    }

}
