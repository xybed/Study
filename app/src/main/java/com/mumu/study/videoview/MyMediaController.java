package com.mumu.study.videoview;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mumu.study.R;

import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Administrator on 2016/12/9.
 */

public class MyMediaController extends MediaController {
    private GestureDetector mGestureDetector;
    private ImageButton img_back;//返回键
    private ImageView img_Battery;//电池电量显示
    private TextView textViewTime;//时间提示
    private TextView textViewBattery;//文字显示电池
    private VideoView videoView;
    private Activity activity;
    private Context context;
    private int controllerWidth = 0;//设置mediaController高度为了使横屏时top显示在屏幕顶端

    //返回监听
    private View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(activity != null){
                activity.finish();
            }
        }
    };
    //videoview 用于对视频进行控制的等，activity为了退出
    public MyMediaController(Context context, VideoView videoView , Activity activity) {
        super(context);
        this.context = context;
        this.videoView = videoView;
        this.activity = activity;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        controllerWidth = wm.getDefaultDisplay().getWidth();
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    protected View makeControllerView() {
        View v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getResources().getIdentifier("mymediacontroller", "layout", getContext().getPackageName()), this);
        v.setMinimumHeight(controllerWidth);
        img_back = (ImageButton) v.findViewById(getResources().getIdentifier("mediacontroller_top_back", "id", context.getPackageName()));
        img_Battery = (ImageView) v.findViewById(getResources().getIdentifier("mediacontroller_imgBattery", "id", context.getPackageName()));
        img_back.setOnClickListener(backListener);
        textViewBattery = (TextView)v.findViewById(getResources().getIdentifier("mediacontroller_Battery", "id", context.getPackageName()));
        textViewTime = (TextView)v.findViewById(getResources().getIdentifier("mediacontroller_time", "id", context.getPackageName()));

        return v;

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        System.out.println("MYApp-MyMediaController-dispatchKeyEvent");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //当收拾结束，并且是单击结束时，控制器隐藏/显示
            toggleMediaControlsVisiblity();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        //双击暂停或开始
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            playOrPause();
            return true;
        }
    }



    public void setTime(String time){
        if (textViewTime != null)
            textViewTime.setText(time);
    }
    //显示电量，
    public void setBattery(String stringBattery){
        if(textViewTime != null && img_Battery != null){
            textViewBattery.setText( stringBattery + "%");
            int battery = Integer.valueOf(stringBattery);
            if(battery < 15)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery));
            if(battery < 30 && battery >= 15)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery));
            if(battery < 45 && battery >= 30)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery));
            if(battery < 60 && battery >= 45)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery));
            if(battery < 75 && battery >= 60)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery));
            if(battery < 90 && battery >= 75)img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery));
            if(battery > 90 )img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery));
        }
    }
    //隐藏/显示
    private void toggleMediaControlsVisiblity(){
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }
    //播放与暂停
    private void playOrPause(){
        if (videoView != null)
            if (videoView.isPlaying()) {
                videoView.pause();
            } else {
                videoView.start();
            }
    }
}
