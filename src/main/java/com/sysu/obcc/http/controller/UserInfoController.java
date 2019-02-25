package com.sysu.obcc.http.controller;

/**
 * @Author: obc
 * @Date: 2019/2/24 20:24
 * @Version 1.0
 */

import com.sysu.obcc.http.pojo.UserInfo;
import com.sysu.obcc.http.response.RespObj;
import com.sysu.obcc.http.service.UserInfoService;
import com.sysu.obcc.http.utils.ErrorCode;

import com.sysu.obcc.http.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户信息处理
 */
@RestController
@RequestMapping(path = "/api", produces = "application/json;charset=utf-8;")
public class UserInfoController {
    @Autowired
    UserInfoService service;

    @RequestMapping(path = "/userInfo", method = RequestMethod.GET)
    public String getUserInfo(String username) {
        UserInfo info = service.getUserInfo(username);
        RespObj obj = new RespObj();

        if (info != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("userInfo", info);
            obj.setData(map);
        } else {
            obj.setCode(ErrorCode.UNKNOWN_ERROR);
        }

        String result = null;
        try {
            result = JsonUtils.bean2Json(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(path = "/modifyInfo")
    public String updateInfo(@RequestParam String username,
                             @RequestParam(required = false) String nickname,
                             @RequestParam(required = false) Integer icon) {
        UserInfo info = new UserInfo();
        info.setUsername(username);
        info.setNickname(nickname);
        info.setIcon(icon);

        boolean flag = service.updateUserInfo(info);
        RespObj obj = new RespObj();
        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("userInfo", info);
            obj.setData(map);
        } else {
            obj.setCode(ErrorCode.UNKNOWN_ERROR);
        }

        String result = null;
        try {
            result = JsonUtils.bean2Json(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
