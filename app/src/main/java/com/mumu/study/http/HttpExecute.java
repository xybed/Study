package com.mumu.study.http;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.mumu.study.BuildConfig;
import com.mumu.study.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpExecute {

    public static final int CACHE_JSON = 1;

    public static final int ERR_CODE_BEAN = -10005;
    public static final int ERR_CODE_JSON_PARSE = -10004;
    public static final int ERR_CODE_NET = -10000;
    public static final int ERR_CODE_UNKNOWN = -10001;
    public static final int ERR_CODE_TIMEOUT = -10002;
    public static final int ERR_CODE_DATA_INTERCEPT = -10003;
    public static final int ERR_CODE_TOKEN = -99;
    public static final int REQ_SUCCESS = 1;
    public static final int REQ_FAILURE = -1;
    public static final int ERR_CODE_JSON_UNKNOW = -10006;

    private final int SUCCESS_CODE = 0;

    private String req_msg = "Detail";
    private String req_return = "Return";

    private static HttpExecute httpExecute;

    private Context mContext;

    private static final String TAG = HttpExecute.class.getSimpleName();
    private OkHttpClient mOkHttpClient;

    public OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }

    public void setmOkHttpClient(OkHttpClient mOkHttpClient) {
        this.mOkHttpClient = mOkHttpClient;
    }

    private HttpExecute(Context context) {
        this.mContext = context;
        mOkHttpClient = new OkHttpClient.Builder().build();
    }

    public static HttpExecute getInstance(Context context) {
        if (httpExecute == null)
            httpExecute = new HttpExecute(context);
        return httpExecute;
    }

    /**
     * http get请求
     *
     * @param clazz
     * @param url
     * @param params
     * @param responseListener
     * @param cacheKey         如果存在cachekey则表示需要进行缓存
     * @param <T>
     */
    public <T> Request get(Class<T> clazz, String url, HttpRequestParams params, String cacheKey,
                           final ResponseListener responseListener) {
       return request(1, clazz, url, params, cacheKey, responseListener);
    }


    public <T> Request post(Class<T> clazz, String url, HttpRequestParams params, String cacheKey, final ResponseListener responseListener) {
        return request(2, clazz, url, params, cacheKey, responseListener);
    }

    /**
     * 新版本的
     */
    public <T> Request getModel(Class<T> clazz, String url, HttpRequestParams params, String cacheKey,
                                final ResponseListener<T> responseListener){
        return request(1,clazz,url,params,cacheKey,responseListener);
    }

    public <T> Request getList(Class<T> clazz, String url, HttpRequestParams params, String cacheKey,
                               final ResponseListener<List<T>> responseListener){
        return request(1,clazz,url,params,cacheKey,responseListener);
    }

    public <T> Request postModel(Class<T> clazz, String url, HttpRequestParams params, String cacheKey,
                                 final ResponseListener<T> responseListener){
        return request(2,clazz,url,params,cacheKey,responseListener);
    }

    public <T> Request postList(Class<T> clazz, String url, HttpRequestParams params, String cacheKey,
                                final ResponseListener<List<T>> responseListener){
        return request(2,clazz,url,params,cacheKey,responseListener);
    }


    private <T> Request request(int method, Class<T> clazz, String url, HttpRequestParams params, String cacheKey,
                                final ResponseListener responseListener) {
        if (method == 1) {
            //get
            String paramString = "";
            for (ConcurrentHashMap.Entry<String, String> entry : params.urlParams.entrySet()) {
                paramString = paramString + "&" + entry.getKey() + "=" + entry.getValue();
            }
            if (!paramString.equals("") && !paramString.equals("?")) {
                //如果包含？说明url中可以直接添加参数：此时如果包含了&说明url中已经存在参数，则再添加新的参数时候需要添加一个&
                url += url.contains("?") ? (url.contains("&") ? "&" : "") : "?";
                //为了标准化url的格式，把paramString的第一个“&”去掉,如果url的最后一个字符为&，则应该去掉；如果为?则也应该去掉
                paramString = paramString.substring(1);
                url += paramString;
            }
            if (BuildConfig.DEBUG) {
                Log.e(TAG + "_初始请求", url);
            }
            Request request = new Request.Builder().url(url).build();
            executeRequest(url, clazz, request, new HttpHandler(responseListener), cacheKey);
            return request;
        } else {
            //post
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (ConcurrentHashMap.Entry<String, String> entry : params.urlParams.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
            for (ConcurrentHashMap.Entry<String, HttpRequestParams.FileWrapper> entry : params.fileParams.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue().customFileName, RequestBody.create(MediaType.parse(entry.getValue().contentType), entry.getValue().file));
            }
//            for (ConcurrentHashMap.Entry<String, HttpRequestParams.StreamWrapper> entry : params.streamParams.entrySet()) {
//                HttpRequestParams.StreamWrapper stream = entry.getValue();
//                byte[] bytes = StreamUtil.inputStream2Byte(stream.inputStream);
//                builder.addFormDataPart(entry.getKey(), stream.name, RequestBody.create(MediaType.parse(stream.contentType), bytes));
//            }
            String logUrl = url + (url.contains("?") ? "" : "?") + params;
            if (BuildConfig.DEBUG) {
                Log.e(TAG + "_初始请求", logUrl);
            }
            Request request = new Request.Builder().url(url).post(builder.build()).build();
            executeRequest(logUrl, clazz, request, new HttpHandler(responseListener), cacheKey);
            return request;
        }
    }

    public void cancelRequest(Request request) {
        mOkHttpClient.newCall(request).cancel();
    }

    private <T> void executeRequest(final String currUrl, final Class<T> clazz, Request request, final HttpHandler handler, final String cacheKey) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                int errCode = ERR_CODE_NET;
                // 失败回调
                sendFailureMsg(currUrl, handler, errCode, "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Log.e("http", responseString);
//                handleSuccessHttpResponse(currUrl, clazz, handler, responseString, cacheKey);
            }
        });
    }

    /**
     * 请求成功后服务端返回的后续处理
     *
     * @param clazz          返回数据对应的泛型类
     * @param handler        View层的回调
     * @param responseString 返回的字符窜
     * @param <T>            bean
     */
    private <T> void handleSuccessHttpResponse(String currUrl, final Class<T> clazz, final HttpHandler handler, final String responseString, final String cacheKey) {
        if (responseString == null || "".equals(responseString)) {
            // 失败回调
            sendFailureMsg(currUrl, handler, ERR_CODE_UNKNOWN, "response is nothing");
            return;
        }

        Object object = null;
        String dataStr = "";
        try {
            if (BuildConfig.DEBUG) {
                //测试人员，才打印请求的解密字符串
                Log.i(TAG + "_请求url", "" + currUrl);
                Log.i(TAG + "_返回数据", "" + responseString);
            }
            JSONObject jsonObject = new JSONObject(responseString);
            int mReturn = Integer.parseInt(jsonObject.getString(req_return));
            if (mReturn != 0) {
                sendFailureMsg(currUrl, handler, mReturn, jsonObject.getString(req_msg));
                return;
            }

            dataStr = jsonObject.getString("Data");
            //非空且不等于“null”
            if (dataStr != null && !"null".equals(dataStr)) {
                boolean isArray = dataStr.startsWith("[");
                boolean isObject = dataStr.startsWith("{");
                if (isArray) {
                    object = JSON.parseArray(dataStr, clazz);
                } else if (isObject) {
                    object = JSON.parseObject(dataStr, clazz, Feature.IgnoreNotMatch, Feature.InitStringFieldAsEmpty);
                } else {
                    object = dataStr;
                }
                sendSuccessMsg(handler, new ResponseModel(SUCCESS_CODE,jsonObject.getString(req_msg),object));
            } else {
                //如果是null，则直接返回空的object
                sendSuccessMsg(handler, new ResponseModel(SUCCESS_CODE,"数据为空",object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendFailureMsg(currUrl, handler, ERR_CODE_BEAN, "json格式不符");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            sendFailureMsg(currUrl, handler, ERR_CODE_JSON_PARSE, "json解析异常");
            return;
        }
    }


    private void sendFailureMsg(String currUrl, Handler handler, int errCode, String errMsg) {
        if (handler == null) {
            return;
        }

        if (errCode == ERR_CODE_TOKEN) {
            Message msg = new Message();
            msg.what = 0;
        }

        if (BuildConfig.DEBUG) {
            Log.e(TAG + "_当前url", currUrl);
            Log.e(TAG + "_错误信息", "errCode=" + errCode + ", errMsg=" + errMsg);
        }

        Message msg = handler.obtainMessage();
        msg.what = REQ_FAILURE;
        msg.obj = new ResponseModel(errCode,errMsg,null);
        handler.sendMessage(msg);
    }

    private void sendSuccessMsg(Handler handler, ResponseModel model) {
        if (handler == null) {
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = REQ_SUCCESS;
        msg.obj = model;
        handler.sendMessage(msg);
    }

}
