package com.sysu.obcc.tcp.dto;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author: obc
 * @Date: 2019/3/4 23:33
 * @Version 1.0
 */

/**
 * 超时重传消息包装类
 */
public class TimeoutTask implements Delayed {

    // 需要重传的消息
    private MessageTask task;

    // 重传时间(ms)
    private long delayTime;

    public TimeoutTask(MessageTask task, long delayTime) {
        this.task = task;
        this.delayTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delayTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long result = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        if (result > 0) {
            return 1;
        }
        else if (result == 0) {
            return 0;
        }
        else {
            return -1;
        }
    }

    public MessageTask getTask() {
        return task;
    }

    public void setTask(MessageTask task) {
        this.task = task;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }
}
