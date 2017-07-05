package com.mumu.study.realm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mumu.study.R;

import io.realm.RealmConfiguration;

public class RealmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);
    }

    private void initRealm(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("test.realm")
                .encryptionKey(new byte[64])
                .schemaVersion(1)
                .build();
    }

    private void insert(){

    }
}
