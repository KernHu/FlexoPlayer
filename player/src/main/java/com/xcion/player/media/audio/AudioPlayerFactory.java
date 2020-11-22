package com.xcion.player.media.audio;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.xcion.player.media.AbstractFactory;
import com.xcion.player.media.Lifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/22/20 4:59 PM
 * describe: This is...
 */

public class AudioPlayerFactory extends AbstractFactory implements Lifecycle<String> {

    private Context context;
    private AudioView mAudioView;

    public AudioPlayerFactory(Context context) {
        this.context = context;
    }

    @Override
    public View getView() {

        AudioView view = new AudioView(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
        lp.leftMargin = 20;
        lp.rightMargin = 20;
        view.setLayoutParams(lp);

        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        mAudioView = (AudioView) view;
    }

    @Override
    public void setMediaTask(ArrayList<String> tasks) {
        setMediaTask(tasks, false);
    }

    @Override
    public void setMediaTask(ArrayList<String> tasks, boolean isAppend) {
        mAudioView.setMediaTask(tasks, isAppend);
        mAudioView.startPlay();
    }

    @Override
    public void startPlay() {
        mAudioView.startPlay();
    }

    @Override
    public void stopPlay() {
        mAudioView.stopPlay();
    }

    @Override
    public void lowMemory() {
        mAudioView.lowMemory();
    }

    @Override
    public void trimMemory(int level) {
        mAudioView.trimMemory(level);
    }

    @Override
    public void recycle() {
        mAudioView.recycle();
    }
}
