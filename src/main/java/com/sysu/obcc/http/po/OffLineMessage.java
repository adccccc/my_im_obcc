package com.sysu.obcc.http.po;

/**
 * @Author: obc
 * @Date: 2019/3/4 20:24
 * @Version 1.0
 */

/**
 * 离线报文实体
 */
public class OffLineMessage {

    private String messageId;

    private int version;

    private long timestamp;

    private String senderId;

    // 群聊消息时此属性为null
    private String receiverId;

    // 好友消息时此属性为null
    private String groupId;

    /**
     * 消息类型
     *  0-WORD 1-IMAGE 2-FILE
     */
    private int type;

    private String content;

    public OffLineMessage() { }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
