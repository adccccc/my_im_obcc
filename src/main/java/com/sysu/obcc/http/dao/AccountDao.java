package com.sysu.obcc.http.dao;

import com.sysu.obcc.http.pojo.Account;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 登录/注册操作Dao
 */
@Mapper
@Repository
public interface AccountDao {

    // 根据username获取账户
    @Select("select * from `account`" +
            " where username = #{username}")
    public Account getAccount(String username);

    // 添加账户
    @Insert("insert into `account`(username, password, salt)" +
            " values(#{username}, #{password}, #{salt}) ")
    public void addAccount(Account account);

    // 查询用户名是否已存在
    @Select("select count(*) from `account`" +
            " where username = #{username}")
    public int getUsernameCount(String username);

    // 更新密码
    @Update("update `account` " +
            " set password  = #{password}, salt = #{salt}" +
            " where username = #{username}")
    public int modifyAccount(Account account);

}
