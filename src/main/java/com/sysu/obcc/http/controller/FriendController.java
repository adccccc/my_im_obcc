package com.sysu.obcc.http.controller;

import com.sysu.obcc.http.pojo.UserInfo;
import com.sysu.obcc.http.response.RespObj;
import com.sysu.obcc.http.service.FriendService;
import com.sysu.obcc.http.utils.ErrorCode;
import com.sysu.obcc.http.utils.JsonUtils;
import com.sysu.obcc.http.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: obc
 * @Date: 2019/2/27 0:00
 * @Version 1.0
 */

/**
 * 好友相关 控制器
 */
@RestController
@RequestMapping(path = "/api", produces = "application/json;charset=utf-8;")
public class FriendController {
    @Autowired
    FriendService friendService;

    @RequestMapping(value = "/friendList", method = RequestMethod.GET)
    public String getFriendList(@RequestParam String username) {
        List<UserInfo> users =  friendService.getFriendList(username);
        RespObj obj = new RespObj();

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("friendList", users);
        obj.setData(map);

        String result = null;
        try {
            result = JsonUtils.bean2Json(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @RequestMapping(value = "/searchUser", method = RequestMethod.GET)
    public String searchUser(@RequestParam String username,
                             @RequestParam("search_name")String searchName) {
        SearchResult searchResult = friendService.searchUser(username, searchName);
        RespObj obj = new RespObj();

        if (searchResult != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("result", searchResult.getResult());
            map.put("friend", searchResult.isFriend());
            obj.setData(map);
        } else {
            obj.setCode(ErrorCode.USER_NOT_EXIST);
        }

        String result = null;
        try {
            result = JsonUtils.bean2Json(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/friendRequest", method = RequestMethod.POST)
    public String requestFriend(@RequestParam String username,
                                @RequestParam String friend,
                                String message) {
        return "";
    }

    @RequestMapping(value = "/handleFriendRequest", method = RequestMethod.POST)
    public String handleRequest(@RequestParam String username,
                                @RequestParam("request_id") String requestId,
                                @RequestParam Boolean handle,
                                String message) {
        return "";
    }

    @RequestMapping(value = "/deleteFriend", method = RequestMethod.POST)
    public String deleteFriend(@RequestParam String username,
                               @RequestParam String friend) {
        return "";
    }
}
