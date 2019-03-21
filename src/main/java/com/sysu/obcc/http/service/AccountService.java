package com.sysu.obcc.http.service;

import com.sysu.obcc.http.dao.AccountDao;
import com.sysu.obcc.http.pojo.Account;
import com.sysu.obcc.http.pojo.UserInfo;
import com.sysu.obcc.http.utils.EncryptUtils;
import com.sysu.obcc.http.utils.TokenUtils;
import jdk.nashorn.internal.parser.Token;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

/**
 * 账户登录信息管理
 */
@Service
public class AccountService {
    @Autowired
    AccountDao accountDao;
    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    UserInfoService userInfoService;

    /**
     * 登录验证
     * @param username
     * @param password
     * @return
     */
    public boolean signIn(String username, String password) {
        Account account = accountDao.getAccount(username);
        if (account == null) return false;
        String encrypted = EncryptUtils.encryptToSHA256(password, account.getSalt());
        boolean flag = encrypted.equals(account.getPassword());
        if (flag) {   // 生成token
            tokenUtils.generateToken(username);
        }
        return flag;
    }

    /**
     * 注册账户,并生成用户信息
     * @param username
     * @param password
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public boolean signUp(String username, String password, String nickname) {
        // 用户名已存在
        if (accountDao.getUsernameCount(username) > 0) return false;

        String salt = EncryptUtils.generateSalt();
        String encryptedPW = EncryptUtils.encryptToSHA256(password, salt);
        Account account = new Account(username, encryptedPW, salt);

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickname(nickname);
        userInfo.setIcon(1);
        userInfo.setRegTime(new Date().getTime());

        try {
            accountDao.addAccount(account);
            userInfoService.addUserInfo(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            // 保证回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    /**
     * 登出
     * 清除token即可
     * @param username
     * @return
     */
    public void signOut(String username) {
        tokenUtils.expireToken(username);
    }

    /**
     * 重置密码
     * 重置完成后需要清除token
     * @param username
     * @param currentPassword
     * @param newPassword
     * @return
     */
    public boolean resetPassword(String username, String currentPassword, String newPassword) {
        Account account = accountDao.getAccount(username);
        if (account == null) return false;

        String encrypted = EncryptUtils.encryptToSHA256(currentPassword, account.getSalt());
        if (encrypted.equals(account.getPassword())) {
            String newSalt = EncryptUtils.generateSalt();
            String newEncryptedPW = EncryptUtils.encryptToSHA256(newPassword, newSalt);
            account.setPassword(newEncryptedPW);
            account.setSalt(newSalt);
            try {
                accountDao.modifyAccount(account);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            tokenUtils.expireToken(username);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取token
     * @param username
     * @return
     */
    public String getToken(String username) {
        return tokenUtils.getToken(username);
    }
}
