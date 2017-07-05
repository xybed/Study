package com.mumu.study.rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.mumu.study.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class RxBindingActivity extends AppCompatActivity {

    @Bind(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_binding);
        ButterKnife.bind(this);
//        Action1<View> clickAction = new Action1<View>() {
//            @Override
//            public void call(View view) {
//                Toast.makeText(RxBindingActivity.this, "", Toast.LENGTH_SHORT).show();
//            }
//        };
        RxView.clicks(button)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(RxBindingActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}
