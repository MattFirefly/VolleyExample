package com.zhanggangmin.volleyexample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.zhanggangmin.volleyexample.frame.Backtrack;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private MyHandler handler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = (MainActivity) reference.get();
            if(activity != null){
                switch (msg.what) {
                    case 200:
                        Backtrack backtrack = (Backtrack) msg.obj;
                        if (backtrack.getState().equals("1")) {
                            Gson gson = new Gson();
//                            List<UserInfo> userInfos = gson.fromJson(backtrack.getDate().toString(), new TypeToken<List<UserInfo>>() {
//                            }.getType());
//                            activity.setView(userInfos.get(0));
                        }else {
                            Toast.makeText(activity, backtrack.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 400:
                        String error = (String) msg.obj;
                        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue requestQueue = Volley.newRequestQueue(this);


    }
}
