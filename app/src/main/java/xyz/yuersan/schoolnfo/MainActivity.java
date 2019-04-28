package xyz.yuersan.schoolnfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import xyz.yuersan.schoolnfo.util.StatusBarUtil;

public class MainActivity extends AppCompatActivity implements OnClickListener  {

    private TextView mTextMessage;
    private ClassFragment classFragment;
    private SchoolFragment schoolFragment;
    private MoreFragment moreFragment;
    private BottomNavigationView navigation;
    private long mPressedTime = 0;
    private PopupWindow popupWindow;
    private int titleHight;
    private int titleWidth;
    private ImageView toolbarAdd;
    private View titleBar;
    private TextView titleText;
    private MenuItem itemClass;
    private MenuItem itemSchool;
    private MenuItem itemMore;

    private static final int THEME_DEFAULT = 1;
    private static final int THEME_OTHER = 2;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            resetToDefaultIcon();
            switch (item.getItemId()) {
                case R.id.navigation_class:
                    item.setIcon(R.drawable.ic_navigation_class);
                    showFrag(R.id.navigation_class);
                    return true;
                case R.id.navigation_school:
                    item.setIcon(R.drawable.ic_navigation_school);
                    showFrag(R.id.navigation_school);
                    return true;
                case R.id.navigation_more:
                    item.setIcon(R.drawable.ic_navigation_more);
                    showFrag(R.id.navigation_more);
                    return true;
            }
            return false;
        }
    };
    private int theme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redrawTheme();
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        initView();
        init();
    }

    private void initView() {
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbarAdd = (ImageView) findViewById(R.id.toolbar_add);
        titleBar = findViewById(R.id.titlebar);
        titleText = (TextView) findViewById(R.id.toolbar_title);
        itemClass = navigation.getMenu().findItem(R.id.navigation_class);
        itemSchool = navigation.getMenu().findItem(R.id.navigation_school);
        itemMore = navigation.getMenu().findItem(R.id.navigation_more);
        initTitleBar();
        navigation.setItemIconTintList(null);
        navigation.getMenu().getItem(0).setIcon(R.drawable.ic_navigation_class);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbarAdd.setOnClickListener(this);
//        setOnRefreshListener();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            titleHight = titleBar.getHeight();
            titleWidth = titleBar.getWidth();
        }
    }

    /**
     * 初始化Fragment
     */
    private void init() {
        classFragment = new ClassFragment();
        schoolFragment = new SchoolFragment();
        moreFragment = new MoreFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content,classFragment).add(R.id.content,schoolFragment).add(R.id.content,moreFragment);//开启一个事务将fragment动态加载到组件
        transaction.hide(classFragment).hide(schoolFragment).hide(moreFragment);//隐藏fragment
        transaction.commit();//每一个事务最后操作必须是commit()
        showFrag(R.id.navigation_class);
    }

    /**
     *通过导航栏点击显示Fragment
     *
     * @param navigationItem 导航栏Item
     */
    private void showFrag(int navigationItem) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (navigationItem){
            case R.id.navigation_class:
                transaction.hide(schoolFragment).hide(moreFragment);
                transaction.show(classFragment);
                transaction.disallowAddToBackStack();
//                transaction.addToBackStack(null);
                transaction.commit();
                titleText.setText(R.string.title_class);
                toolbarAdd.setVisibility(View.VISIBLE);
                break;
            case R.id.navigation_school:
                transaction.hide(classFragment).hide(moreFragment);
                transaction.show(schoolFragment);
                transaction.disallowAddToBackStack();
//                transaction.addToBackStack(null);
                transaction.commit();
                titleText.setText(R.string.title_school);
                toolbarAdd.setVisibility(View.VISIBLE);
                break;
            case R.id.navigation_more:
                transaction.hide(classFragment).hide(schoolFragment);
                transaction.show(moreFragment);
                transaction.disallowAddToBackStack();
//                transaction.addToBackStack(null);
                transaction.commit();
                titleText.setText(R.string.title_more);
                toolbarAdd.setVisibility(View.GONE);
                break;
        }

    }

    /**
     * 重置导航栏item图标
     */
    private void resetToDefaultIcon(){
        itemClass.setIcon(R.drawable.ic_navigation_class_gray);
        itemSchool.setIcon(R.drawable.ic_navigation_school_gray);
        itemMore.setIcon(R.drawable.ic_navigation_more_gray);
    }
    private void showPopWindow() {
        int layoutResource = 0;
        if (itemClass.isChecked())
            layoutResource = R.layout.fragment_class_popupwindow;
        else if (itemSchool.isChecked())
            layoutResource = R.layout.fragment_school_popupwindow;

        View view = LayoutInflater.from(this).inflate(layoutResource,null);
        int i = titleWidth * 2 / 5;
        popupWindow = new PopupWindow(titleBar, i, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        addBackground();
        //设置进出动画

        View parentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        popupWindow.showAtLocation(titleBar,Gravity.TOP|Gravity.END,0,titleHight);
    }
    private void addBackground() {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//调节透明度
        getWindow().setAttributes(lp);
        //dismiss时恢复原样
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == toolbarAdd){
            showPopWindow();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        redrawTheme();
        initTitleBar();
    }

    private void redrawTheme() {
        SharedPreferences sp = getSharedPreferences("Theme",
                Context.MODE_PRIVATE);
        if (sp == null){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("theme",1);
            editor.apply();
        } else {
            theme = sp.getInt("theme", 1);
            if (theme == THEME_DEFAULT){
                setTheme(R.style.AppTheme);
            } else if (theme == THEME_OTHER){
                setTheme(R.style.RedsTheme);
            }
        }
    }

    private void initTitleBar() {
        if (theme == THEME_OTHER)
            titleBar.setBackgroundResource(R.drawable.rtitlebar);
        else if(theme == THEME_DEFAULT)
            titleBar.setBackgroundResource(R.drawable.titlebar);
    }
}
