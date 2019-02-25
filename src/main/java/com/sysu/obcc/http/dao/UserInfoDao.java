package com.sysu.obcc.http.dao;

import com.sysu.obcc.http.pojo.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Author: obc
 * @Date: 2019/2/24 15:45
 * @Version 1.0
 */
@Mapper
@Repository
public interface UserInfoDao {

    @Insert("insert into `user_info`(username, nickname, icon, reg_time)" +
            " values(#{username}, #{nickname}, #{icon}, #{regTime})")
    public void addUserInfo(UserInfo info);

    @Select("select" +
            " username," +
            " nickname," +
            " icon," +
            " reg_time as regTime" +
            " from `user_info`" +
            " where username = #{username}")
    public UserInfo getUserInfo(String username);


    @Update("<script>" +
            " update `user_info` " +
            " <set>" +
            " <if test = \"nickname != null\">" +
                " nickname = #{nickname}," +
            " </if>" +
            " <if test = \"icon != null\">" +
                " icon = #{icon}" +
            " </if>" +
            " </set>" +
            " where username = #{username}" +
            "</script>")
    public void updateUserInfo(UserInfo info);
}
