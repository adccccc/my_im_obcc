package com.sysu.obcc.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysu.obcc.http.response.RespObj;
import com.sysu.obcc.http.service.AccountService;
import com.sysu.obcc.http.utils.ConstantUtils;
import com.sysu.obcc.http.utils.ErrorCode;
import com.sysu.obcc.http.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录 注册
 */
@RestController
@RequestMapping(path = "/api", produces = "application/json;charset=utf-8;")
public class LoginController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public String signIn(String username, String password) {
        boolean flag = accountService.signIn(username, password);
        RespObj respObj = new RespObj();

        if (flag) {
            String token = accountService.getToken(username);
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("token", token);
            respObj.setData(map);
        } else {
            respObj.setCode(ErrorCode.WRONG_PASSWORD);
        }

        String result = null;
        try {
            result = JsonUtils.bean2Json(respObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public String signUp(String username, String password, String nickname) {
        boolean flag = accountService.signUp(username, password, nickname);
        RespObj obj = new RespObj();

        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            obj.setData(map);
        } else {
            obj.setCode(ErrorCode.OCCUPIED_USERNAME);
        }

        String result = null;
        try {
            result = JsonUtils.bean2Json(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @RequestMapping(value = "/signOut")
    public String signOut(String username) {
        accountService.signOut(username);
        RespObj obj = new RespObj();

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        obj.setData(map);

        String result = null;
        try {
            result = JsonUtils.bean2Json(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPW(String username, String curPW, String resetPW) {
        boolean flag = accountService.resetPassword(username, curPW, resetPW);
        RespObj obj = new RespObj();

        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            obj.setData(map);
        } else {
            obj.setCode(ErrorCode.WRONG_PASSWORD);
        }

        String result = null;
        try {
            result = JsonUtils.bean2Json(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    @RequestMapping("/test")
//    public String test(String a, String b, HttpServletRequest request) {
//        return b;
//    }
}
