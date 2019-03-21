package com.sysu.obcc.tcp.service;

import com.sysu.obcc.http.dao.SingleMessageDao;
import com.sysu.obcc.http.po.OffLineMessage;
import com.sysu.obcc.tcp.dto.MessageTask;
import com.sysu.obcc.tcp.proto.CcPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * 将消息报文处理成离线消息并存储到db
     * @param messageTask
     */
    public void storeMessageTask(MessageTask messageTask) {
        if (messageTask == null) return;
        switch (messageTask.getType()) {
            case SINGLE_CHAT:   // 好友聊天消息
                storeSingleMessage(messageTask);
                break;
            case GROUP_CHAT:    // 群聊消息
                break;
            case FRIEND_EVENT:  // 好友事件消息
                break;
            case GROUP_EVENT:   // 群组事件消息
                break;
        }
    }

    /**
     * 存储离线好友消息
     * @param messageTask
     */
    public void storeSingleMessage(MessageTask messageTask) {
        CcPacket.SingleChatPacket singleChatPacket = (CcPacket.SingleChatPacket)messageTask.getMessageLite();
        OffLineMessage message = new OffLineMessage(singleChatPacket);
        singleMessageDao.addMessage(message);
    }

    /**
     * 拉取用户的所有离线的好友消息并删除
     * @param username
     */
    public List<OffLineMessage> getOfflineMessageList(String username) {
        List<OffLineMessage> singleList = singleMessageDao.getMessages(username);
        singleMessageDao.deleteMessages(username);
        return singleList;
    }


    public SingleMessageDao getSingleMessageDao() {
        return singleMessageDao;
    }

    public void setSingleMessageDao(SingleMessageDao singleMessageDao) {
        this.singleMessageDao = singleMessageDao;
    }


}
