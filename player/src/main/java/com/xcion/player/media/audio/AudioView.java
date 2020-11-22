package com.xcion.player.media.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.xcion.player.media.Lifecycle;
import com.xcion.player.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/22/20 6:49 PM
 * describe: This is...
 */

@SuppressLint("AppCompatCustomView")
public class AudioView extends ImageView implements Lifecycle<String> {

    private RotateAnimation mRotateAnimation;
    private AudioPlayer mAudioPlayer;
    private ArrayList<String> mAudioTasks = new ArrayList<>();

    public AudioView(Context context) {
        this(context, null);
    }

    public AudioView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AudioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_phonograph));
        //
        mRotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);//围绕自身的中心旋转
        mRotateAnimation.setInterpolator(new LinearInterpolator());//匀速旋转
        mRotateAnimation.setDuration(3000);
        mRotateAnimation.setRepeatCount(-1);//无限重复
        this.startAnimation(mRotateAnimation);

        mAudioPlayer = new AudioPlayer();
    }

    @Override
    public void setMediaTask(ArrayList<String> tasks) {
        setMediaTask(tasks, false);
    }

    @Override
    public void setMediaTask(ArrayList<String> tasks, boolean isAppend) {
        mAudioTasks.clear();
        mAudioTasks.addAll(tasks);
    }

    @Override
    public void startPlay() {
        //
        mRotateAnimation.start();
        //
        mAudioPlayer.startPlay(mAudioTasks.get(0));
    }

    @Override
    public void stopPlay() {
        //
        mRotateAnimation.cancel();
        //
        mAudioPlayer.stopPlay();
    }

    @Override
    public void lowMemory() {

    }

    @Override
    public void trimMemory(int level) {

    }

    @Override
    public void recycle() {
        //
        mRotateAnimation.cancel();
        //
        mAudioPlayer.recycle();
    }
}
