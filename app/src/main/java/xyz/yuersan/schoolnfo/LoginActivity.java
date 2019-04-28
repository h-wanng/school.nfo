package xyz.yuersan.schoolnfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
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
import xyz.yuersan.schoolnfo.util.StatusBarUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private long mPressedTime = 0;
    private Button btnLogin;
    private EditText editAccount;
    private EditText editPassword;
    private ImageView headImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        initView();
    }

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }
        else{
            this.finish();
            System.exit(0);
        }
    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        editAccount = (EditText) findViewById(R.id.login_edit_account);
        editPassword = (EditText) findViewById(R.id.login_edit_password);
        headImg = (ImageView) findViewById(R.id.login_head);
        btnLogin.setOnClickListener(this);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int w = x * 5 / 8;
        int rwidth = x / 4;
        ViewGroup.LayoutParams editPwdLP = editPassword.getLayoutParams();
        ViewGroup.LayoutParams editAccountLP = editAccount.getLayoutParams();
        ViewGroup.LayoutParams btnLP = btnLogin.getLayoutParams();
        ViewGroup.LayoutParams headImgLP = headImg.getLayoutParams();
        editAccountLP.width = w;
        editPwdLP.width = w;
        btnLP.width = w;
        headImgLP.width = rwidth;
        headImgLP.height = rwidth;
        editAccount.setLayoutParams(editAccountLP);
        editPassword.setLayoutParams(editPwdLP);
        btnLogin.setLayoutParams(btnLP);
        headImg.setLayoutParams(headImgLP);
        SharedPreferences sp = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        if (sp != null) {
            String account = sp.getString("Account", "");
            String password = sp.getString("Password", "");
            editAccount.setText(account);
            editPassword.setText(password);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            String account = editAccount.getText().toString();
            String password = editPassword.getText().toString();
            if (account.equals("") || password.equals("")){
                Toast.makeText(getApplicationContext(), "请输入账号/密码", Toast.LENGTH_SHORT).show();
            } else {
                httpLogin(account, password);
            }
        }
    }

    /**
     *  网络登录
     * @param account 账号
     * @param password 密码
     */

    private void httpLogin(String account, String password) {
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
                Toast.makeText(getApplicationContext(), "网络出错,稍后重试", Toast.LENGTH_SHORT).show();
                Looper.loop();
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
                    String id = jsonObject.getString("message");
                    Log.d("UIDUIDUIDUIDUIDUID", id);
                    SharedPreferences sp = getSharedPreferences("Login",
                            Context.MODE_PRIVATE);
                    Editor editor = sp.edit();
                    editor.putString("Id",id);
                    editor.putString("Account", account);
                    editor.putString("Password", password);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd", Locale.CHINA);
                    String date = dateFormat.format(new Date());
                    editor.putString("Date", date);
                    editor.apply();
                    loginSuccess();
                } else if (jsonObject.getString("state").equals("error")){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "账号密码错误,请重试", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    /**
     * 登录成功跳转activity
     */
    private void loginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
}
