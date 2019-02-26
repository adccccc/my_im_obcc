package com.sysu.obcc.http.dao;

/**
 * @Author: obc
 * @Date: 2019/2/26 22:58
 * @Version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 封装一下FriendDao
 */
@Component
public class FriendDaoImpl{
    @Autowired
    FriendDao friendDao;

    /**
     * 获取好友列表
     * @param username
     * @return
     */
    public List<String> getFriendList(String username) {
        return friendDao.getFriendList(username);
    }

    /**
     * 新增好友关系
     * @param user1
     * @param user2
     * @return
     */
    public boolean addFriend(String user1, String user2) {
        if (user1.equals(user2)) return false;

        if (user1.compareTo(user2) > 0) {
            swap(user1, user2);
        }

        try {
            friendDao.addFriend(user1, user2, new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除好友关系
     * 这里需要注意 不存在的关系也会返回true
     * @param user1
     * @param user2
     * @return
     */
    public boolean deleteFriend(String user1, String user2) {
        if (user1.equals(user2)) return false;

        if (user1.compareTo(user2) > 0) {
            swap(user1, user2);
        }

        friendDao.deleteFriend(user1, user2);
        return true;
    }

    /**
     * 验证是否好友关系
     * @param user1
     * @param user2
     * @return
     */
    public boolean isFriends(String user1, String user2) {
        if (user1.equals(user2)) return false;

        if (user1.compareTo(user2) > 0) {
            swap(user1, user2);
        }

        return friendDao.isFriends(user1, user2) > 0;
    }

    private void swap(String str1, String str2) {
        String temp = str1;
        str1 = str2;
        str2 = temp;
    }
}
