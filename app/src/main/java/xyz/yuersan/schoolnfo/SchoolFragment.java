package xyz.yuersan.schoolnfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.yuersan.schoolnfo.adapter.ClassGroupAdapter;
import xyz.yuersan.schoolnfo.adapter.SchoolnfoAdapter;
import xyz.yuersan.schoolnfo.model.ClassModel;
import xyz.yuersan.schoolnfo.model.InfoModel;
import xyz.yuersan.schoolnfo.util.HttpUtil;


public class SchoolFragment extends Fragment {
    private static final int COMPLETED = 1;
    private List<InfoModel> mInfoDatas;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private SchoolnfoAdapter schoolnfoAdapter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == COMPLETED)
                initSchoolInfoRecycleView();
            return false;
        }
    });

    public SchoolFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_school, container, false);
        mInfoDatas = new ArrayList<InfoModel>();
        initView();
        initSchoolInfoData();

        return view;
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.school_refreshlayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent);
        setOnRefreshListener();
    }


    private void initSchoolInfoData() {
        String url = "http://10.0.2.2:8080/School.nfo/info/schoolinfoget";
        RequestBody requestBody = new FormBody.Builder()
                .build();

        HttpUtil.sendRequestWithOkhttp(requestBody, url, new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure: ", e.getMessage());
                Looper.prepare();
                Toast.makeText(getContext(), "网络出错,稍后重试", Toast.LENGTH_SHORT).show();
                Looper.loop();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJson = response.body().string();
                Log.d("ClassResponse",responseJson);
//                JSONObject jsonObject = JSONObject.parseObject(responseJson);

//                mInfoDatas.clear();
                mInfoDatas = JSONObject.parseArray(responseJson, InfoModel.class);
                Log.d("SchoolInfo", "size"+mInfoDatas.size()+mInfoDatas.get(0).getInfoTitle());
                refreshLayout.setRefreshing(false);
                Message message = new Message();
                message.what = COMPLETED;
                handler.sendMessage(message);
            }
        });
    }


    private void initSchoolInfoRecycleView(){
        RecyclerView schoolRecycleView = (RecyclerView) view.findViewById(R.id.school_recycleview);
        EditText search = (EditText) view.findViewById(R.id.search_schoolnfo);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_search);
        drawable.setBounds(0, 0, 35, 35);
        search.setCompoundDrawables(drawable, null, null, null);
        SpannableString ss = new SpannableString("搜索");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        search.setHint(new SpannedString(ss));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        schoolRecycleView.setLayoutManager(layoutManager);
        schoolRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        schoolnfoAdapter = new SchoolnfoAdapter(mInfoDatas, getContext());
        schoolRecycleView.setAdapter(schoolnfoAdapter);
    }

    private void setOnRefreshListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initSchoolInfoData();
            }
        });
    }
}
