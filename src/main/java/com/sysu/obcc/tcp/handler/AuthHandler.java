package com.sysu.obcc.tcp.handler;

import com.sysu.obcc.http.utils.TokenUtils;
import com.sysu.obcc.tcp.proto.CcPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: obc
 * @Date: 2019/3/2 15:53
 * @Version 1.0
 */

/**
 * 连接建立后，校验身份
 * 在认证完成前，拦截所有报文
 * 连接认证完成后自动移除校验
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    TokenUtils tokenUtils;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        boolean authResult = false;
        if (msg instanceof CcPacket.AuthPacket) {
            CcPacket.AuthPacket authPacket = (CcPacket.AuthPacket) msg;
            String userId = authPacket.getUserId();
            String token = authPacket.getToken();
            authResult = tokenUtils.verifyToken(userId, token);     // 验证token
        }
        if (authResult) {       // 验证成功

            ctx.pipeline().remove(this);    // 移除校验

        } else {        // 验证失败

        }

    }

    /**
     * 此handler被移除后的响应
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}
