package com.sysu.obcc.tcp.handler;

import com.google.protobuf.MessageLite;
import com.sysu.obcc.tcp.dto.MessageTask;
import com.sysu.obcc.tcp.proto.CcPacket;
import com.sysu.obcc.tcp.proto.PacketType;
import com.sysu.obcc.tcp.service.RepeatMsgService;
import com.sysu.obcc.tcp.utils.MsgIdUtils;
import com.sysu.obcc.tcp.utils.MsgQueueUtils;
import com.sysu.obcc.tcp.utils.PacketUtils;
import com.sysu.obcc.tcp.utils.UserStatusManager;
import io.netty.channel.Channel;
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

    private static AttributeKey<String> key = AttributeKey.newInstance("latest_msg_id");

    @Autowired
    private RepeatMsgService repeatMsgService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CcPacket.SingleChatPacket msg) throws Exception {

        // 判断是否为新报文
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
            String receiverId = packet.getReceiverId();
            // 获取接收方channel, 不在线时为null
            Channel forwardChannel = UserStatusManager.getInstance().getChannelByUserId(receiverId);

            // 包装转发task
            MessageTask task = new MessageTask();
            task.setMessageId(packet.getMessageId());
            // 无需考虑在线状态，转发线程有对应处理
            task.setChannel(forwardChannel);
            task.setReceiverId(receiverId);
            task.setMessageLite(packet);
            task.setType(PacketType.SINGLE_CHAT);

            // 添加task到转发队列
            try {
                MsgQueueUtils.addMessageTask(task);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("消息转发时失败：" + task.toString());
            }
        }

    }
}
