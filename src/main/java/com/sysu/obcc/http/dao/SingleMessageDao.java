package com.sysu.obcc.http.dao;

/**
 * @Author: obc
 * @Date: 2019/3/4 20:52
 * @Version 1.0
 */

import com.sysu.obcc.http.po.OffLineMessage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 离线好友消息dao
 */
@Mapper
@Component
public interface SingleMessageDao {

    @Select(" select message_id as messageId," +
            " version," +
            " send_time as timestamp," +
            " sender_id as senderId," +
            " receiver_id as receiverId," +
            " type," +
            " content from `offline_single_message`" +
            " where receiver_id = #{userId}")
    public List<OffLineMessage> getMessages(String userId);

    @Insert(" insert ignore into" +
            " `offline_single_message`(message_id, version, send_time, sender_id, receiver_id, type, content)" +
            " values(#{messageId}, #{version}, #{timestamp}, #{senderId}, #{receiverId}, #{type}, #{content})")
    public void addMessage(OffLineMessage message);

    @Delete(" delete from `offline_single_message`" +
            " where message_id = #{messageId}")
    public void delete(String messageId);
}
