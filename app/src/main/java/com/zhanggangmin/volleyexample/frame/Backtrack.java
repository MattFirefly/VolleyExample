package com.zhanggangmin.volleyexample.frame;

import com.google.gson.JsonArray;

/**
 * Created by Administrator on 2015-11-06.
 * 返回参数
 */
public class Backtrack {
    private String state;
    private String msg;
    private JsonArray date;

    public Backtrack(JsonArray date, String state, String msg) {
        this.date = date;
        this.state = state;
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public JsonArray getDate() {
        return date;
    }

    public void setDate(JsonArray date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
