package com.sysu.obcc.http.vo;

/**
 * @Author: obc
 * @Date: 2019/2/26 22:40
 * @Version 1.0
 */

import com.sysu.obcc.http.pojo.UserInfo;

/**
 * 搜索用户结果包装类
 */
public class SearchResult {

    public UserInfo result;

    public boolean friend;

    public SearchResult() {
    }

    public UserInfo getResult() {
        return result;
    }

    public void setResult(UserInfo result) {
        this.result = result;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }
}
