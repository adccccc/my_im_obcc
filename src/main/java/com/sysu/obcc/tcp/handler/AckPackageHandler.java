package com.sysu.obcc.tcp.handler;

/**
 * @Author: obc
 * @Date: 2019/3/12 18:41
 * @Version 1.0
 */

import com.sysu.obcc.tcp.proto.CcPacket;
import com.sysu.obcc.tcp.utils.MsgQueueUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Ack报文处理
 */
public class AckPackageHandler extends SimpleChannelInboundHandler<CcPacket.AckPacket> {

    /**
     * 确认消息送达，将重传任务从超时队列中移除即可
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CcPacket.AckPacket msg) throws Exception {
        String messageId = msg.getAckId();
        MsgQueueUtils.confirmMessage(messageId);
    }
}
