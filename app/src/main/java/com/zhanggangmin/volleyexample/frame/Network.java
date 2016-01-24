package com.zhanggangmin.volleyexample.frame;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhanggangmin on 16/1/16.
 */
public class Network {
    public static final String HOST = "http://app.5199buy.cn:8810";
    private static final String TAG = "GM";
    private static Gson gson;
    private static Network ourInstance;
    private JsonObject jsonObject;
    private RequestQueue mRequestQueue;
    private Backtrack req;
    private Message msg;

    private Network(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static Network getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new Network(context);
            gson = new Gson();

        }
        return ourInstance;
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelCaChe(){
        if (mRequestQueue != null) {
            mRequestQueue.getCache().clear();
        }
    }

    public void getSequenceNumber(){
        if (mRequestQueue != null) {
            mRequestQueue.getSequenceNumber();
        }
    }

    private void connection(final Handler handler, final Object param, final String url, Object tag, final boolean isCache) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("response: " + response);

                jsonObject = new JsonParser().parse(response).getAsJsonObject();
                req = new Backtrack(jsonObject.getAsJsonArray("data"), jsonObject.get("state").getAsString(), jsonObject.get("msg").getAsString());

                msg = Message.obtain();
                msg.what = 200;
                msg.obj = req;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                msg = Message.obtain();
                msg.what = 400;
                if (isCache) {
                    if (mRequestQueue.getCache().get(HOST + url) != null) {
                        msg.obj = new String(mRequestQueue.getCache().get(HOST + url).data);
                    }
                } else {
                    msg.obj = VolleyErrorHelper.getMessage(error);
                    //msg.obj = error.toString();
                    //msg.obj = "网络异常，请检查数据连接！";
                }
                handler.sendMessage(msg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                System.out.println("request " + gson.toJson(param));

                // 在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("param", gson.toJson(param));
                return map;
            }
        };
        stringRequest.setTag(tag == null ? TAG : tag);//设置清除标签

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));//超时再请求一次

        mRequestQueue.add(stringRequest);
    }

    private void connection(final Handler handler, Objects param, String url, Objects tag) {
        String json = gson.toJson(param);
        JSONObject params = new JSONObject();
        try {
            params.put("param", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

        });
        jsonObjectRequest.setTag(tag);
        mRequestQueue.add(jsonObjectRequest);
    }

}
