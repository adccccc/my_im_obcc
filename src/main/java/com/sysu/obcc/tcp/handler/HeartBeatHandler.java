package com.sysu.obcc.tcp.handler;

import com.sysu.obcc.tcp.proto.CcPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: obc
 * @Date: 2019/3/4 13:46
 * @Version 1.0
 */

/**
 * 处理心跳报文
 */
public class HeartBeatHandler extends SimpleChannelInboundHandler<CcPacket.HeartBeatPacket> {

    /**
     * 直接发送一个Ack报文即可
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CcPacket.HeartBeatPacket msg) throws Exception {
        CcPacket.AuthPacket.Builder builder = CcPacket.AuthPacket.newBuilder();
        builder.setMessageId(msg.getMessageId());
        ctx.writeAndFlush(builder.build());
    }

}
