package com.sysu.obcc.tcp.utils;

/**
 * @Author: obc
 * @Date: 2019/3/4 23:10
 * @Version 1.0
 */

import com.sysu.obcc.tcp.dto.MessageTask;
import com.sysu.obcc.tcp.dto.TimeoutTask;
import io.netty.util.Timeout;

import java.sql.Time;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * 简陋的消息队列
 */
public class MsgQueueUtils {

    // 等待转发报文队列
    private static final BlockingQueue<MessageTask> blockingQueue = new ArrayBlockingQueue<MessageTask>(1024);

    // 超时重传报文队列
    private static final DelayQueue delayQueue = new DelayQueue();

    // 等待Ack报文id与重传Task的映射
    private static final Map<String, TimeoutTask> taskMap = new ConcurrentHashMap<>();

    /**
     * 将消息存入转发队列
     * @param task
     * @throws InterruptedException
     */
    public static void addMessageTask(MessageTask task) throws InterruptedException {
        blockingQueue.put(task);
    }

    /**
     * 从转发队列中取出消息
     * @return
     * @throws InterruptedException
     */
    public static MessageTask getNextMessageTask() throws InterruptedException{
        return blockingQueue.take();
    }

    /**
     * 收到Ack报文后确认消息
     * 从超时队列中移除任务
     * @param messageId
     */
    public static void confirmMessage(String messageId) {
        TimeoutTask task = taskMap.get(messageId);
        if (task != null) {
            try {
                delayQueue.remove(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
            taskMap.remove(messageId);
        }
    }

    /**
     * 加入Task到超时队列
     * @param task
     */
    public static void addTimeoutTask(TimeoutTask task) {
        taskMap.put(task.getTask().getMessageId(), task);
        delayQueue.put(task);
    }

    /**
     * 取出一个已经超时的Task，并将taskMap中记录移除
     * @return
     * @throws InterruptedException
     */
    public static TimeoutTask getNextTimeoutTask() throws InterruptedException{
        TimeoutTask task = (TimeoutTask) delayQueue.take();
        taskMap.remove(task.getTask().getMessageId());
        return task;
    }
}
