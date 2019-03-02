package com.sysu.obcc.tcp.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

/**
 * @Author: obc
 * @Date: 2019/3/2 17:16
 * @Version 1.0
 */

/**
 * 自定义protobuf解码器
 * 支持非指定message
 */
@ChannelHandler.Sharable
public class CustomProtobufDecoder extends ByteToMessageDecoder {

    /**
     * todo: length 类型 int --> varInt32
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > 5) {    // 4byte length + 1byte type
            in.markReaderIndex();

            int length = in.readInt();      // 获取报文长度
            byte type = in.readByte();      // 获取报文类型

            if (in.readableBytes() < length) {      // 拆包则恢复读指针并退出
                in.resetReaderIndex();
                return;
            }

            ByteBuf body = in.readBytes(length);    // 读取body

            byte[] array;
            int offset;

            int readableLen = body.readableBytes();
            if (body.hasArray()) {
                array = body.array();
                offset = body.arrayOffset() + body.readerIndex();
            } else {
                array = new byte[readableLen];
                body.getBytes(body.readerIndex(), array, 0, readableLen);
                offset = 0;
            }

            // 反序列化
            MessageLite result = decodeBody(type, array, offset, readableLen);
            out.add(result);
        }
    }

    /**
     * 反序列化
     * todo: 改掉这丑陋的if...else
     * @param type
     * @param array
     * @param offset
     * @param readableLen
     * @return
     * @throws Exception
     */
    private MessageLite decodeBody(byte type, byte[] array, int offset, int readableLen) throws Exception {
        if (type == 0x01) {
            return CcPacket.AuthPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x02) {
            return CcPacket.HeartBeatPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x03) {
            return CcPacket.SingleChatPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x04) {
            return CcPacket.GroupChatPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x05) {
            return CcPacket.FriendEventPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x06) {
            return CcPacket.GroupEventPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x07) {
            return CcPacket.AckPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x08) {
            return CcPacket.AuthErrorPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x09) {
            return CcPacket.InvalidChatPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        } else if (type == 0x0a) {
            return CcPacket.InvalidEventPacket.getDefaultInstance().getParserForType().parseFrom(array, offset, readableLen);
        }
        throw new InvalidProtocolBufferException("找不到解析类型");
    }
}
