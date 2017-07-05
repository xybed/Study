package com.mumu.study.jni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mumu.study.R;

public class JniActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        textView = (TextView) findViewById(R.id.text_view);
        NdkJniUtils ndkJniUtils = new NdkJniUtils();
        textView.setText(ndkJniUtils.getCLanguageString());
    }
}
