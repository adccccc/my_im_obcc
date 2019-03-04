package com.sysu.obcc.tcp.handler;

import com.google.protobuf.MessageLite;
import com.sysu.obcc.tcp.proto.CcPacket;
import com.sysu.obcc.tcp.service.RepeatMsgService;
import com.sysu.obcc.tcp.utils.PacketUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.jar.Attributes;

/**
 * @Author: obc
 * @Date: 2019/3/4 16:08
 * @Version 1.0
 */

/**
 * 好友聊天消息处理
 */
public class SingleChatPackageHandler extends SimpleChannelInboundHandler<CcPacket.SingleChatPacket> {

    @Autowired
    private RepeatMsgService repeatMsgService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CcPacket.SingleChatPacket msg) throws Exception {

        // 判断是否为新报文
        AttributeKey<String> key = AttributeKey.newInstance("latest_msg_id");
        boolean isLatest = false;
        if (ctx.channel().hasAttr(key)) {
            String latestId = ctx.channel().attr(key).get();
            if (latestId.compareTo(msg.getMessageId()) <= 0) {  // 最新报文
                ctx.channel().attr(key).set(msg.getMessageId());
                isLatest = true;
            } else {    // 报文重复
                MessageLite messageLite = repeatMsgService.getAck(msg.getMessageId());  // 获取缓存Ack
                if (messageLite != null) {
                    ctx.channel().writeAndFlush(messageLite);
                } else {
                    isLatest = true;        // 缓存已过期，也需要重新处理(基本不存在此情况)
                }
            }
        } else {
            ctx.channel().attr(key).set(msg.getMessageId());
            isLatest = true;
        }

        if (isLatest) {     // 处理报文
            CcPacket.AckPacket ack = PacketUtils.generateAck(msg.getMessageId(), null);
            ctx.channel().writeAndFlush(ack);   // 回复Ack报文

            CcPacket.SingleChatPacket packet = PacketUtils.generateSingleChat(msg);
            
        }

    }
}
