package com.xcion.player.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xcion.player.Lifecycle;
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


    private AudioPlayer mAudioPlayer;
    ArrayList<String> mAudioTasks = new ArrayList<>();

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

        mAudioPlayer = new AudioPlayer();
    }

    @Override
    public void setMediaTask(List<String> tasks) {
        setMediaTask(tasks, false);
    }

    @Override
    public void setMediaTask(List<String> tasks, boolean isAppend) {
        mAudioTasks.clear();
        mAudioTasks.addAll(tasks);
    }

    @Override
    public void startPlay() {
        mAudioPlayer.startPlay(mAudioTasks.get(0));
    }

    @Override
    public void stopPlay() {
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
        mAudioPlayer.recycle();
    }
}
