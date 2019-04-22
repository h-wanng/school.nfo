package xyz.yuersan.schoolnfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import xyz.yuersan.schoolnfo.util.StatusBarUtil;
import xyz.yuersan.schoolnfo.util.SystemBarTintManager;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ClassFragment classFragment;
    private SchoolFragment schoolFragment;
    private MoreFragment moreFragment;
    private BottomNavigationView navigation;
    private long mPressedTime = 0;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        init();
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);
        navigation.getMenu().getItem(0).setIcon(R.drawable.ic_navigation_class);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    private void showFrag(int navigationItem) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (navigationItem){
            case R.id.navigation_class:
                transaction.hide(schoolFragment).hide(moreFragment);
                transaction.show(classFragment);
                transaction.disallowAddToBackStack();
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.navigation_school:
                transaction.hide(classFragment).hide(moreFragment);
                transaction.show(schoolFragment);
                transaction.disallowAddToBackStack();
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.navigation_more:
                transaction.hide(classFragment).hide(schoolFragment);
                transaction.show(moreFragment);
                transaction.disallowAddToBackStack();
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }

    }

    private void resetToDefaultIcon(){
        MenuItem itemClass = navigation.getMenu().findItem(R.id.navigation_class);
        MenuItem itemSchool = navigation.getMenu().findItem(R.id.navigation_school);
        MenuItem itemMore = navigation.getMenu().findItem(R.id.navigation_more);
        itemClass.setIcon(R.drawable.ic_navigation_class_gray);
        itemSchool.setIcon(R.drawable.ic_navigation_school_gray);
        itemMore.setIcon(R.drawable.ic_navigation_more_gray);
    }


}
