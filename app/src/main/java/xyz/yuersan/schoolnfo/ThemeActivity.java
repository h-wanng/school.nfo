package xyz.yuersan.schoolnfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import xyz.yuersan.schoolnfo.util.StatusBarUtil;

public class ThemeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private View defaultTheme;
    private View otherTheme;
    private CheckBox checkDefault;
    private CheckBox checkOther;

    private static final int THEME_DEFAULT = 1;
    private static final int THEME_OTHER = 2;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("Theme",
                Context.MODE_PRIVATE);
        if (sp != null) {
            int theme = sp.getInt("theme", 1);
            if (theme == THEME_DEFAULT){
                setTheme(R.style.AppTheme);
            } else if (theme == THEME_OTHER){
                setTheme(R.style.RedsTheme);
            }
        }
        setContentView(R.layout.activity_theme);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        initView();
    }

    private void initView() {
        sp = getSharedPreferences("Theme",
                Context.MODE_PRIVATE);
        int theme = sp.getInt("theme", 1);
        back = (ImageView) findViewById(R.id.back);
        defaultTheme = findViewById(R.id.default_theme_frame);
        otherTheme = findViewById(R.id.other_theme_frame);
        checkDefault = (CheckBox) findViewById(R.id.checkbox_theme_default);
        checkOther = (CheckBox) findViewById(R.id.checkbox_theme_other);
        View titleBar = findViewById(R.id.titlebar);
        if (theme == THEME_OTHER)
            titleBar.setBackgroundResource(R.drawable.rtitlebar);
        else if(theme == THEME_DEFAULT)
            titleBar.setBackgroundResource(R.drawable.titlebar);
        back.setOnClickListener(this);
        defaultTheme.setOnClickListener(this);
        otherTheme.setOnClickListener(this);
        if (theme == THEME_DEFAULT) {
            checkDefault.setChecked(true);
            checkOther.setChecked(false);
        } else if (theme == THEME_OTHER) {
            checkDefault.setChecked(false);
            checkOther.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == back){
            onBackPressed();
            this.finish();
        }
        if (v == defaultTheme){
            if (checkDefault.isChecked()){
                Toast.makeText(this, "正在使用",Toast.LENGTH_SHORT).show();
            } else {
                Editor editor = sp.edit();
                editor.putInt("theme", 1);
                editor.apply();
                checkDefault.setChecked(true);
                checkOther.setChecked(false);
                back.performClick();
            }
        }
        if (v == otherTheme){
            if (checkOther.isChecked()){
                Toast.makeText(this, "正在使用",Toast.LENGTH_SHORT).show();
            } else {
                Editor editor = sp.edit();
                editor.putInt("theme", 2);
                editor.apply();
                checkDefault.setChecked(false);
                checkOther.setChecked(true);
                back.performClick();
            }
        }
    }
}
