package xyz.yuersan.schoolnfo.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    public static void sendRequestWithOkhttp(RequestBody requestBody, String url, okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
