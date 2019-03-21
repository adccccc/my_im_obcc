package com.sysu.obcc.tcp.handler;

import com.sysu.obcc.http.utils.ConstantUtils;
import com.sysu.obcc.tcp.utils.UserStatusManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Date;
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
        String userId = UserStatusManager.getInstance().getUserIdByChannel(ctx.channel());
        System.out.println(new Date().toString() + ": 用户[" + userId+ "]心跳响应超时" );
        ctx.channel().close();
    }

    /**
     * 任何情况下channel关闭后都会移除此handler，在这里进行用户离线处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 离线处理
        String userId = UserStatusManager.getInstance().getUserIdByChannel(ctx.channel());
        UserStatusManager.getInstance().setOffline(ctx.channel());
        System.out.println("用户[" + userId + "]" + "已下线");
        super.handlerRemoved(ctx);
    }
}
