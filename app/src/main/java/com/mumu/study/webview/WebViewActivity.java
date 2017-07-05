package com.mumu.study.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ZoomButtonsController;

import com.mumu.study.R;

import java.lang.reflect.Field;

public class WebViewActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private WebView webContent;

    private boolean canZoom = true;
    private String url = "http://www.jinmishijia.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initUI();
        initSetting();
    }

    private void initUI(){
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webContent = (WebView) findViewById(R.id.web_content);
    }

    private void initSetting(){
        webContent.getSettings().setJavaScriptEnabled(true);
        webContent.getSettings().setUseWideViewPort(false);
        //设置手动缩放使能
        webContent.getSettings().setBuiltInZoomControls(canZoom);
        webContent.getSettings().setSupportZoom(canZoom);
        if (canZoom) {
            //设置第一次加载页面时候的大小
            webContent.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
            //开始第一次加载界面时候，隐藏，放大缩小按键，此按键在手动触发滑动事件的时候将再次出现
            setZoomControlGone(webContent);
        }
        //设置支持用户手动输入用户名密码等，获取手势焦点
        webContent.requestFocusFromTouch();
        webContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//0);
        webContent.requestFocus();
        webContent.setWebViewClient(new MyWebViewClient());
        webContent.setDownloadListener(new MyDownloadStart());//添加下载文件跳转，暂时使用外部浏览器下载

        webContent.getSettings().setAllowFileAccess(true);
        webContent.getSettings().setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

        //启用地理定位
        webContent.getSettings().setGeolocationEnabled(true);
        //设置定位的数据库路径
        webContent.getSettings().setGeolocationDatabasePath(dir);

        //最重要的方法，一定要设置，这就是出不来的主要原因
        webContent.getSettings().setDomStorageEnabled(true);
        webContent.getSettings().setSaveFormData(false);
        webContent.getSettings().setAppCacheEnabled(false);//webView不缓存
        webContent.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webContent.getSettings().setLoadWithOverviewMode(false);//<==== 一定要设置为false，不然有声音没图像
        //清除缓存，处理默写手机浏览器显示白页问题
        webContent.clearCache(true);

        //进度条进度修改
        webContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);
                //这里将textView换成你的progress来设置进度
                if (progressBar == null) return;
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
                progressBar.postInvalidate();
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webContent.loadUrl(url);
    }

    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            field.set(view, mZoomButtonsController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyWebViewClient extends WebViewClient{}

    class MyDownloadStart implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            //调用系统浏览器下载
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
