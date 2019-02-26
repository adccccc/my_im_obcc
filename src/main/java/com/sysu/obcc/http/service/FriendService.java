package com.sysu.obcc.http.service;

import com.sysu.obcc.http.dao.FriendDao;
import com.sysu.obcc.http.dao.FriendDaoImpl;
import com.sysu.obcc.http.dao.UserInfoDao;
import com.sysu.obcc.http.pojo.UserInfo;
import com.sysu.obcc.http.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: obc
 * @Date: 2019/2/26 22:26
 * @Version 1.0
 */

/**
 * 好友管理
 */
@Service
public class FriendService {

    @Autowired
    FriendDaoImpl friendDao;
    @Autowired
    UserInfoDao userInfoDao;

    /**
     * 获取好友列表
     * @param username
     * @return
     */
    public List<UserInfo> getFriendList(String username) {
        List<String> nameList = friendDao.getFriendList(username);
        List<UserInfo> userList = new ArrayList<>();
        for (String name: nameList) {
            UserInfo info = userInfoDao.getUserInfo(name);
            if (info != null) userList.add(info);
        }
        return userList;
    }

    /**
     * 通过用户名搜索用户，并验证是否为好友
     * 找不到则返回null
     * @param user1
     * @Param user2 要搜索的用户名
     * @return
     */
    public SearchResult searchUser(String user1, String user2) {

        SearchResult searchResult = null;
        UserInfo user = userInfoDao.getUserInfo(user2);
        if (user != null) {
            boolean f = friendDao.isFriends(user1, user2);
            searchResult = new SearchResult();
            searchResult.setFriend(f);
            searchResult.setResult(user);
        }
        return searchResult;
    }

    public boolean addRequest(String user1, String user2, String message) {
        return true;

    }

    public boolean handleRequest(String username, String requestId, boolean handle, String message) {
        return true;
    }

    public boolean deleteFriend(String user1, String user2) {
        return friendDao.deleteFriend(user1, user2);
    }


}
