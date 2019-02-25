package com.sysu.obcc.http.service;

/**
 * @Author: obc
 * @Date: 2019/2/24 16:01
 * @Version 1.0
 */

import com.sysu.obcc.http.dao.UserInfoDao;
import com.sysu.obcc.http.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理用户信息
 */
@Service
public class UserInfoService {

    @Autowired
    UserInfoDao userInfoDao;

    /**
     * get
     * @param username
     * @return
     */
    public UserInfo getUserInfo(String username) {
        return userInfoDao.getUserInfo(username);
    }

    /**
     * 注册账号时，新增userInfo
     *
     * @param info
     * @return
     */
    public boolean addUserInfo(UserInfo info) {
        try {
            userInfoDao.addUserInfo(info);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新用户信息
     * @param info
     * @return
     */
    public boolean updateUserInfo(UserInfo info) {
        try {
            userInfoDao.updateUserInfo(info);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
