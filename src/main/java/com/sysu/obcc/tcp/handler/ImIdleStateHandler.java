package com.sysu.obcc.tcp.handler;

import com.sysu.obcc.http.utils.ConstantUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author: obc
 * @Date: 2019/3/4 13:29
 * @Version 1.0
 */

/**
 * 空闲检测
 */
public class ImIdleStateHandler extends IdleStateHandler {


    public ImIdleStateHandler() {
        super(ConstantUtils.TCP_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    /**
     * TCP_IDLE_TIME s 内未读到数据，则关闭此channel
     */
    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        //Todo: log
        ctx.channel().close();
    }
}
