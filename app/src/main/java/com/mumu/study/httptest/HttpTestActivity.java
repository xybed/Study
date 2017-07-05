package com.mumu.study.httptest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mumu.study.R;
import com.mumu.study.http.HttpExecute;
import com.mumu.study.http.HttpRequestParams;
import com.mumu.study.http.ResponseListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HttpTestActivity extends AppCompatActivity {

    @Bind(R.id.text_view)
    TextView textView;

    private List<String> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);
        ButterKnife.bind(this);

        testList = new ArrayList<>();
    }

    private void request() {
        Log.e("http", "开始请求");
        Toast.makeText(this, "开始请求", Toast.LENGTH_SHORT).show();
        HttpRequestParams params = new HttpRequestParams();
        params.put("uid", "lantian");
        HttpExecute.getInstance(this).postModel(String.class, "http://webservice.huiwork.com/fujian/index.php/user/api/getIndexAdv", params, "", new ResponseListener<String>() {
            @Override
            public void onSuccess(String object) {

            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                Log.e("http", errMsg);
            }
        });
    }

    @OnClick({R.id.button})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
//                request();
                int size = testList.size();
                Log.e("size", ""+size);
                break;
        }

    }
}
