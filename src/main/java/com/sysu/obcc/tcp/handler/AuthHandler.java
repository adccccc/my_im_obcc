package com.sysu.obcc.tcp.handler;

import com.sysu.obcc.http.po.OffLineMessage;
import com.sysu.obcc.http.utils.SpringContextUtils;
import com.sysu.obcc.http.utils.TokenUtils;
import com.sysu.obcc.tcp.dto.MessageTask;
import com.sysu.obcc.tcp.proto.CcPacket;
import com.sysu.obcc.tcp.proto.PacketType;
import com.sysu.obcc.tcp.service.OffLineMsgService;
import com.sysu.obcc.tcp.utils.MsgQueueUtils;
import com.sysu.obcc.tcp.utils.MyThreadPoolManager;
import com.sysu.obcc.tcp.utils.PacketUtils;
import com.sysu.obcc.tcp.utils.UserStatusManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        boolean authResult = false;
        CcPacket.AuthPacket authPacket = null;
        if (msg instanceof CcPacket.AuthPacket) {
            authPacket = (CcPacket.AuthPacket) msg;
            String userId = authPacket.getUserId();
            String token = authPacket.getToken();
            authResult = TokenUtils.verifyToken(userId, token);     // 验证token
        }
        if (authResult) {       // 验证成功
            // 标记用户为online状态
            UserStatusManager.getInstance().setOnline(authPacket.getUserId(), ctx.channel());
            ctx.writeAndFlush(PacketUtils.generateAck(((CcPacket.AuthPacket) msg).getMessageId(), ""));
            System.out.println(new Date().toString() + ":用户["+ authPacket.getUserId() + "]连接成功");
            ctx.pipeline().remove(this);    // 移除校验
            pullOfflineMessages(authPacket.getUserId());

        } else {        // 验证失败, 关闭channel
            if (authPacket != null) {
                ctx.writeAndFlush(PacketUtils.generateAuthErrorPacket(authPacket));
            }
            ctx.channel().close();
        }

    }

    /**
     * 拉取离线消息
     * @param userId
     */
    private void pullOfflineMessages(String userId) {
        MyThreadPoolManager.getInstance().execute(() -> {
            OffLineMsgService offLineMsgService =
                    (OffLineMsgService) SpringContextUtils.getBean("offLineMsgService", OffLineMsgService.class);
            // 拉取离线好友消息
            // TODO : 拉取离线群聊消息
            List<OffLineMessage> messageList = offLineMsgService.getOfflineMessageList(userId);
            for (OffLineMessage msg : messageList) {
                MessageTask task = new MessageTask();
                task.setMessageId(msg.getMessageId());
                task.setReceiverId(msg.getReceiverId());
                task.setChannel(UserStatusManager.getInstance().getChannelByUserId(userId));
                task.setType(PacketType.SINGLE_CHAT);
                task.setMessageLite(PacketUtils.generateSingleChat(msg));
                try {
                    MsgQueueUtils.addMessageTask(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
