package com.sysu.obcc.tcp.service;

/**
 * @Author: obc
 * @Date: 2019/3/3 13:33
 * @Version 1.0
 */

import com.google.protobuf.MessageLite;
import com.sysu.obcc.http.utils.ConstantUtils;
import com.sysu.obcc.http.utils.RedisUtils;
import com.sysu.obcc.tcp.proto.CcPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 缓存近期的ACK报文
 * 如果收到重复报文，获取缓存Ack直接应答
 * todo: 兼容所有种类Ack报文
 */
@Service
public class RepeatMsgService {
    @Autowired
    RedisUtils redisUtils;

    public boolean storeAck(String messageId, CcPacket.AckPacket message) {
        return redisUtils.set(ConstantUtils.MSG_ACK_PREFIX + messageId, message, ConstantUtils.ACK_EXPIRE);
    }

    public CcPacket.AckPacket getAck(String messageId) {
        return (CcPacket.AckPacket)redisUtils.get(ConstantUtils.MSG_ACK_PREFIX + messageId);
    }
}
