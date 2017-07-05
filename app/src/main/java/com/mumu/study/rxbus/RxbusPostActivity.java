package com.mumu.study.rxbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Produce;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.mumu.study.R;

public class RxbusPostActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbus_post);

        RxBus.get().register(this);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.get().post("rxbus", "rxbus使用成功");
            }
        });
    }

//    @Produce(
//            thread = EventThread.IO,
//            tags = {
//                    @Tag("rxbus")
//            }
//    )
//    public String post(){
//        return "produce使用成功";
//    }

//    public void rx(){
//        RxBus.get().post("rxbus", RxbusActivity.class);
//    }


    @Override
    protected void onDestroy() {
        RxBus.get().unregister(this);
        super.onDestroy();
    }
}
