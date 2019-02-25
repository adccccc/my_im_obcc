package com.sysu.obcc.http.pojo;

/**
 * @Author: obc
 * @Date: 2019/2/24 15:42
 * @Version 1.0
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * 用户信息类
 */
public class UserInfo {

    // 用户名
    String username;
    // 昵称
    String nickname;
    // 头像编号
    Integer icon;

    // 注册时间
    @JsonIgnore
    Long regTime;

    public UserInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Long getRegTime() {
        return regTime;
    }

    public void setRegTime(Long regTime) {
        this.regTime = regTime;
    }
}
