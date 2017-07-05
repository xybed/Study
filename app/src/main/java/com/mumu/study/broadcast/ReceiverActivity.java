package com.mumu.study.broadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mumu.study.R;

public class ReceiverActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSendBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        initUI();
    }

    private void initUI(){
        btnSendBroadcast = (Button) findViewById(R.id.btn_send_broadcast);
        btnSendBroadcast.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send_broadcast:
                Intent intent = new Intent();
                intent.setAction("com.mumu.study.MyBroadcastReceiver");
                sendBroadcast(intent);
                break;
        }
    }

}
