package xyz.yuersan.schoolnfo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private static int COUNTDOWN = 5;
    private TextView textCountDown;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                COUNTDOWN--;
                textCountDown.setText("跳过 "+COUNTDOWN+" ");
                if (COUNTDOWN>0){
                    Message message = handler.obtainMessage(1);
                    handler.sendMessageDelayed(message, 1000);
                } else {
                    finishSplash();
                }
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        ImageView imgSplash = (ImageView) findViewById(R.id.img_splash);
        textCountDown = (TextView) findViewById(R.id.text_countdown);
        textCountDown.setOnClickListener(this);
//        imgSplash.setImageResource(R.drawable.bg_splash);
        int time = 3000;
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message,1);
    }
    private void finishSplash(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v == textCountDown){
            handler.removeMessages(1);
            finishSplash();
        }
    }
}
