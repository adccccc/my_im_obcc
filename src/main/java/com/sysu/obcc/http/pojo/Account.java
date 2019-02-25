package com.sysu.obcc.http.pojo;

/**
 * 登录账户实体
 */
public class Account {

    // 账号
    private String username;
    // 密码(hash)
    private String password;
    // 加密盐值
    private String salt;

    public Account(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public Account() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


}
