package com.sysu.obcc.tcp.thread;

/**
 * @Author: obc
 * @Date: 2019/3/5 13:31
 * @Version 1.0
 */

import com.sysu.obcc.http.po.OffLineMessage;
import com.sysu.obcc.http.utils.ConstantUtils;
import com.sysu.obcc.http.utils.SpringContextUtils;
import com.sysu.obcc.tcp.dto.MessageTask;
import com.sysu.obcc.tcp.dto.TimeoutTask;
import com.sysu.obcc.tcp.service.OffLineMsgService;
import com.sysu.obcc.tcp.utils.MsgQueueUtils;
import com.sysu.obcc.tcp.utils.MyThreadPoolManager;
import com.sysu.obcc.tcp.utils.UserStatusManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 转发消息线程
 */
public class ForwardMessageThread implements Runnable {

    private static OffLineMsgService offLineMsgService =
            (OffLineMsgService) SpringContextUtils.getBean("offLineMsgService", OffLineMsgService.class);

    @Override
    public void run() {

        while (true) {
            MessageTask task = null;
            try {
                task = MsgQueueUtils.getNextMessageTask();
            } catch (InterruptedException e) {
                System.out.println("转发线程获取task时发生异常");
                e.printStackTrace();
            }

            if (task == null) {
                continue;
            }

            Channel channel = task.getChannel();
            boolean success = false;

            if (channel != null) {
                if (channel.isActive()) {       // channel非空且active，正常发送消息
                    try {
                        channel.writeAndFlush(task.getMessageLite());       // 发送消息
                        success = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("发送报文失败： MessageID = " + task.getMessageId());
                    }
                } else {
                    // channel非空但inActive，需要再检测一次用户是否在线，如在线则获取新的channel
                    // 这里是为了处理用户掉线重连之后，重传task的channel已经过期的情况
                    String userId = task.getReceiverId();
                    if (UserStatusManager.getInstance().isOnline(userId)) {
                        // 已经重新连接，获取新的channel
                        task.setChannel(UserStatusManager.getInstance().getChannelByUserId(userId));
                        // 将task重新放到转发队列尾部
                        try {
                            MsgQueueUtils.addMessageTask(task);
                            // 结束处理
                            continue;
                        } catch (Exception e) {
                            e.printStackTrace();
                            success = false;
                        }
                    }
                }
            }

            if (success) {      // 发送成功，添加消息到超时队列
                TimeoutTask timeoutTask = new TimeoutTask(task, ConstantUtils.TASK_TIMEOUT);
                MsgQueueUtils.addTimeoutTask(timeoutTask);
            } else {    // 发送失败，存储离线消息(新线程)
                final MessageTask mTask = task;
                MyThreadPoolManager.getInstance().execute(() -> {
                    try {
                        offLineMsgService.storeSingleMessage(mTask);
                    } catch (Exception e) {
                        System.out.println("存储离线消息失败: messageId = "+mTask.getMessageId());
                        e.printStackTrace();
                    }
                });

            }

        }

    }
}
