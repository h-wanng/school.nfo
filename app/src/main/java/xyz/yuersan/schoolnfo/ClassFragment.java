package xyz.yuersan.schoolnfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.yuersan.schoolnfo.adapter.ClassGroupAdapter;
import xyz.yuersan.schoolnfo.model.ClassModel;
import xyz.yuersan.schoolnfo.util.HttpUtil;


public class ClassFragment extends Fragment {
    private static final int COMPLETED = 1;
    private List<ClassModel> mClassDatas;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private ClassGroupAdapter classGroupAdapter;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == COMPLETED)
                classGroupAdapter.notifyDataSetChanged();
            return false;
        }
    });


    public ClassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class, container, false);
        mClassDatas = new ArrayList<ClassModel>();
        Log.d("ClassFragment:",new Date().toString());
        initView();
        initClassData();
        initClassRecycleView();
        return view;
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.class_refreshlayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent);
        setOnRefreshListener();
    }

    private void initClassData(){
        String url = "http://10.0.2.2:8080/School.nfo/classget";
        SharedPreferences sp = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        String id = sp.getString("Id", "");
        RequestBody requestBody = new FormBody.Builder()
                .add("uid", id)
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
                JSONObject jsonObject = JSONObject.parseObject(responseJson);

                ClassModel classModel = JSONObject.parseObject(responseJson, ClassModel.class);
                mClassDatas.clear();
                mClassDatas.add(classModel);
                Log.d("ClassDes", classModel.getClassDes()+mClassDatas.size());
                refreshLayout.setRefreshing(false);
                Message message = new Message();
                message.what = COMPLETED;
                handler.sendMessage(message);
            }
        });
    }

    private void initClassRecycleView(){
        RecyclerView classRecycleView = (RecyclerView) view.findViewById(R.id.class_recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        classRecycleView.setLayoutManager(layoutManager);
        classGroupAdapter = new ClassGroupAdapter(mClassDatas, getContext());
        classRecycleView.setAdapter(classGroupAdapter);
    }

    private void setOnRefreshListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refreshLayout.setRefreshing(false);
//                    }
//                },2000);
                initClassData();
            }
        });
    }

}
