package com.sysu.obcc.tcp.thread;

/**
 * @Author: obc
 * @Date: 2019/3/5 14:08
 * @Version 1.0
 */

import com.sysu.obcc.tcp.dto.MessageTask;
import com.sysu.obcc.tcp.dto.TimeoutTask;
import com.sysu.obcc.tcp.utils.MsgQueueUtils;

/**
 * 重传消息线程
 */
public class RetransmissionThread implements Runnable {
    @Override
    public void run() {
        while (true) {
            TimeoutTask task = null;

            try {   // 获取超时未接收到ack的task
                task = MsgQueueUtils.getNextTimeoutTask();
            } catch (Exception e) {
                System.out.println("超时重传线程获取task时发生异常");
                e.printStackTrace();
            }

            if (task != null) {     // 重传，将报文放入转发队列即可
                try {
                    MsgQueueUtils.addMessageTask(task.getTask());
                } catch (Exception e) {
                    System.out.println("重传时发生异常");
                    e.printStackTrace();
                }
            }
        }
    }
}
