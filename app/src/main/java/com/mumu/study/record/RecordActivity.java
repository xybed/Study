package com.mumu.study.record;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.mumu.study.R;
import com.mumu.study.webview.FileUtil;

import java.io.File;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {

    private Button startRecord;
    private Button playRecord;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        startRecord = (Button) findViewById(R.id.start_record);
        playRecord = (Button) findViewById(R.id.play_record);

        startRecord.setOnTouchListener(new OnRecordTouchListener());
        playRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(file.getAbsolutePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                });
            }
        });

    }

    class OnRecordTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("record", "开始录音");
                    startRecord();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    return true;
                case MotionEvent.ACTION_UP:
                    Log.e("record", "结束录音");
                    stopRecord();
                    return true;
                default:
                    return false;
            }
        }
    }

    private void startRecord(){
        file = new File(FileUtil.getMyCacheDir(FileUtil.Audio),System.currentTimeMillis()+"audio.mp3");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void stopRecord(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }
}
