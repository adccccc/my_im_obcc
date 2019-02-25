package com.sysu.obcc.http.response;

/**
 * @Author: obc
 * @Date: 2019/2/23 22:20
 * @Version 1.0
 */

import java.util.HashMap;
import java.util.Map;

/**
 * api接口返回包装类
 */
public class RespObj {

    private int code;
    private Map<String, Object> data;

    public RespObj(int code, Map<String, Object> data) {
        this.code = code;
        this.data = data;
    }

    public RespObj() {
        code = 0;
        data = new HashMap<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
