package bk.it.com.demo.Utils;


import android.content.Context;
import android.icu.text.UFormat;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * TODO
 * 作者：小马
 * h给你封装的异步请求，你只要类名点方法，然后传入2个参数。 传入一个你要请求的URL地址，和new一个Callback，在返回成功里面做相应的逻辑处理就行
 */

public class HttpUtil {

    private BaseActivity activity;
    private static OkHttpClient client = new OkHttpClient();
    private Gson mGson;
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final String TAG = HttpUtil.class.getSimpleName();
    private static final String BASE_URL = "http://xxx.com/openapi";//请求接口根地址
    private static volatile HttpUtil mInstance;//单利引用
    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_FORM = 2;//post请求参数为表单
    private OkHttpClient mOkHttpClient;//okHttpClient 实例
    private static Handler okHttpHandler;//全局处理子线程和M主线程通信


    //测试提交的数据
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 初始化
     *
     * @param applicationContext
     */
    public HttpUtil(Context applicationContext) {
        client = new OkHttpClient();
        //设置超时时间
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        //设置写入超时时间
        client.setWriteTimeout(20, TimeUnit.SECONDS);
        //设置读取超时时间
        client.setReadTimeout(20, TimeUnit.SECONDS);
    }


    /**
     * 设置缓存
     *
     * @param mContext
     * @return
     */
    public HttpUtil setCache(Context mContext) {
        File sdcache = mContext.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        mOkHttpClient.setCache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        return setCache(mContext.getApplicationContext());
    }


    /**
     * 获取单例引用
     *
     * @return
     */
    public static HttpUtil getInstance(Context context) {
        HttpUtil inst = mInstance;
        if (inst == null) {
            synchronized (HttpUtil.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new HttpUtil(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
    }









    /**
     * POST请求,注意请求地址
     *
     * @param url
     * @param map
     * @param callBacks
     */
    public static void requestNet(String url, Map<String, String> map, final CallBacks callBacks) {
        if (url == null || callBacks == null) {
            return;
        }

        //参数传递
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Set<String> keySet = map.keySet();
        for (String i : keySet) {
            //从集合中一一取到相应的key和value
            String str = map.get(i);
            builder.add(i, str);
        }
        //创建一个Request
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        //请求加入调度
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBacks.onError(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                callBacks.onSuccess(str);
            }
        });
    }


    /**
     * GET异步请求
     *
     * @param address
     * @param callback
     */

    public static void getAsyn(String address, CallBacks callback) {


        Request request = new Request.Builder()
                .get()
                .url(address)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //在这里对异常进行处理
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //得到服务器返回的具体内容
                String responseData = response.body().string();
            }
        });
    }


    /**
     * http的下载文件请求
     *
     * @param downloadUrl
     */

    public static void downloadRequest(String downloadUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //下载失败后的逻辑操作
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //下载成功后执行操作
            }
        });
    }


    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    /**
     * okHttp post异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    public static <T> Call requestPostByAsyn(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            String requestUrl = String.format("%s/%s", BASE_URL, actionUrl);
            final Request request = addHeaders().url(requestUrl).post(body).build();
            final Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });

        } catch (Exception e) {

        }
        return null;
    }


    public interface ReqCallBack<T> {
        /**
         * 响应成功
         */
        void onReqSuccess(T result);

        /**
         * 响应失败
         */
        void onReqFailed(String errorMsg);
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     * @param <T>
     */
    private static <T> void successCallBack(final T result, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     * @param <T>
     */
    private static <T> void failedCallBack(final String errorMsg, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqFailed(errorMsg);
                }
            }
        });
    }


    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private static Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0");
        return builder;
    }

    /**
     * \
     * 网络请求回调接口
     */

    public interface CallBacks {
        /**
         * 请求成功的回调
         *
         * @param message
         */
        public void onSuccess(String message);

        /**
         * 请求失败的回调
         *
         * @param e
         * @param
         */
        public void onError(Request request, Exception e);
    }

}
