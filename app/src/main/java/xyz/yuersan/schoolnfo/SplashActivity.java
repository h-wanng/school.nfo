package xyz.yuersan.schoolnfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.yuersan.schoolnfo.util.HttpUtil;

public class SplashActivity extends AppCompatActivity {
    private boolean loginedBefore = false;
//    private static int COUNTDOWN = 5;
//    private TextView textCountDown;

//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            if (msg.what == 1) {
//                COUNTDOWN--;
//                textCountDown.setText("跳过 "+COUNTDOWN+" ");
//                if (COUNTDOWN>0){
//                    Message message = handler.obtainMessage(1);
//                    handler.sendMessageDelayed(message, 1000);
//                } else {
//                    finishSplash();
//                }
//            }
//            return false;
//        }
//    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
//        ImageView imgSplash = (ImageView) findViewById(R.id.img_splash);
//        textCountDown = (TextView) findViewById(R.id.text_countdown);
//        textCountDown.setOnClickListener(this);
//        imgSplash.setImageResource(R.drawable.bg_splash);
        int time = 3000;
//        Message message = handler.obtainMessage(1);
//        handler.sendMessageDelayed(message,1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("Login",
                        Context.MODE_PRIVATE);
                if (sp != null) {
                    String dateString = sp.getString("Date", "");
                    String account = sp.getString("Account", "");
                    String password = sp.getString("Password", "");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd", Locale.CHINA);
                    try {
                        Date date = dateFormat.parse(dateString);
                        Date nowDate = new Date();
                        long time = date.getTime();
                        long nowTime = nowDate.getTime();
                        long day = (nowTime - time) / (24 * 60 * 60 * 1000);
                        if (day > 7) {
                            Editor edit = sp.edit();
                            edit.clear();
                            edit.apply();
                            loginedBefore = false;
                        } else {
                            String url = "http://10.0.2.2:8080/School.nfo/login";
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("loginName", account)
                                    .add("userPwd", password)
                                    .build();

                            HttpUtil.sendRequestWithOkhttp(requestBody, url, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("onFailure: ", e.getMessage());
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "网络出错,稍后再试", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    loginedBefore = false;
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.d("Response:", response.protocol() + " " + response.code() + " " + response.message());
                                    Headers headers = response.headers();
                                    for (int i = 0; i < headers.size(); i++) {
                                        Log.d("Response:", headers.name(i) + ":" + headers.value(i));
                                    }
                                    String responseJson = response.body().string();
                                    Log.d("OnResponse:", "onResponse: " + responseJson);
                                    JSONObject jsonObject = JSONObject.parseObject(responseJson);
                                    if (jsonObject.getString("state").equals("success")) {
                                        loginedBefore = true;
                                    } else if (jsonObject.getString("state").equals("error")){
                                        Looper.prepare();
                                        Toast.makeText(getApplicationContext(), "账号密码错误,请重试", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                        loginedBefore = false;
                                    }
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    loginedBefore = false;
                }
            }
        });
        thread.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishSplash();
            }
        },time);


    }
    private void finishSplash(){
        Log.d("LoginedBefore", ""+loginedBefore);
        if (loginedBefore) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }


//    @Override
//    public void onClick(View v) {
//        if (v == textCountDown){
//            handler.removeMessages(1);
//            finishSplash();
//        }
//    }
}
