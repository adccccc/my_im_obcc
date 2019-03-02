package com.sysu.obcc.http.interceptor;

import com.sysu.obcc.http.response.RespObj;
import com.sysu.obcc.http.utils.ErrorCode;
import com.sysu.obcc.http.utils.JsonUtils;
import com.sysu.obcc.http.utils.TokenUtils;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: obc
 * @Date: 2019/2/25 23:21
 * @Version 1.0
 */

/**
 * 自定义拦截器检验token
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    TokenUtils tokenUtils;

    /**
     * 从request中获取token，并校验
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = null;

        // 先从cookies中找token
        Cookie[] cookies =  request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 如果没有，再到header中找
        if (token == null) {
            token = request.getHeader("token");
        }

        String username = request.getParameter("username");
        boolean result = tokenUtils.verifyToken(username, token);
        // 验证失败，直接将json写到response并返回
        if (!result) {
            RespObj obj = new RespObj();
            obj.setCode(ErrorCode.INVALID_TOKEN);
            returnJson(response, JsonUtils.bean2Json(obj));
        }
        return result;
    }

    /**
     * 写入json至response
     * @param response
     * @param json
     * @throws Exception
     */
    private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
