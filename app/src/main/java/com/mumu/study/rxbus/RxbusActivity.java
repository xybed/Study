package com.mumu.study.rxbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Produce;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.mumu.study.R;

public class RxbusActivity extends AppCompatActivity {
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbus);

        RxBus.get().register(this);
        textView = (TextView) findViewById(R.id.text_view);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RxbusActivity.this, RxbusPostActivity.class));
//                rx();
            }
        });
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("rxbus")
            }
    )
    public void get(String arg){
        textView.setText(arg);
    }

//    @Produce(
//            thread = EventThread.IO,
//            tags = {
//                    @Tag("rxbus")
//            }
//    )
//    public String post(){
//        return "rxbus使用成功";
//    }


    @Override
    protected void onDestroy() {
        RxBus.get().unregister(this);
        super.onDestroy();
    }
}
