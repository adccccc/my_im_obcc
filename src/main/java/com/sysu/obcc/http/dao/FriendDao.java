package com.sysu.obcc.http.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: obc
 * @Date: 2019/2/26 22:12
 * @Version 1.0
 */

/**
 * 好友管理Dao
 */

@Mapper
@Repository
public interface FriendDao {

    @Select("select user2 from `friend_relation`" +
            " where user1 = #{username}" +
            " union all" +
            " select user1 from `friend_relation`" +
            " where user2 = #{username}")
    public List<String> getFriendList(String username);

    /**
     * 添加好友关系
     * 需保证user1 < user2
     * @param user1
     * @param user2
     * @param time
     */
    @Insert("insert into `friend_relation`(user1, user2, add_time)" +
            " values(#{user1}, #{user2}, #{time})")
    public void addFriend(String user1, String user2, long time);

    /**
     * 删除好友关系
     * 需保证user1 < user2
     * @param user1
     * @param user2
     */
    @Delete("delete from `friend_relation`" +
            " where user1 = #{user1} and user2 = #{user2}")
    public void deleteFriend(String user1, String user2);


    /**
     * 检查两个用户是否为好友关系
     * 如果是好友，返回1 否则返回0
     * 需保证user1 < user2
     * @param user1
     * @param user2
     * @return
     */
    @Select("select count(*) from `friend_relation`" +
            " where user1 = #{user1} and user2 = #{user2}")
    public int isFriends(String user1, String user2);

}
