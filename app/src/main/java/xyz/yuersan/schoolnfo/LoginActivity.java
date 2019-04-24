package xyz.yuersan.schoolnfo;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import xyz.yuersan.schoolnfo.util.StatusBarUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }
}
