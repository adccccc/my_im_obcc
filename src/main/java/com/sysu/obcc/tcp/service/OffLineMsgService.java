package com.sysu.obcc.tcp.service;

import com.sysu.obcc.http.dao.SingleMessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.DelayQueue;

/**
 * @Author: obc
 * @Date: 2019/3/4 21:18
 * @Version 1.0
 */

/**
 * 离线消息处理服务
 */
@Service
public class OffLineMsgService {
    @Autowired
    private SingleMessageDao singleMessageDao;

    DelayQueue delayQueue;

    public void delay() {
        delayQueue.remove(null);
    }

}
