package com.sysu.obcc.tcp.utils;

/**
 * @Author: obc
 * @Date: 2019/3/4 23:10
 * @Version 1.0
 */

import com.sysu.obcc.tcp.dto.MessageTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

/**
 * 简陋的消息队列
 */
public class MsgQueueUtils {

    // 等待发送报文队列
    private static final BlockingQueue<MessageTask> blockingQueue = new ArrayBlockingQueue<MessageTask>(1024);

    // 超时重传报文队列
    private static final DelayQueue queue = new DelayQueue();

    public static BlockingQueue<MessageTask> getForwardQueue() {
        return blockingQueue;
    }

    public static DelayQueue getTimeoutQueue() {
        return queue;
    }



}
