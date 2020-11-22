package com.xcion.player.audio;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xcion.player.AbstractFactory;
import com.xcion.player.Lifecycle;
import com.xcion.player.R;
import com.xcion.player.pojo.MediaTask;
import com.xcion.player.taskbar.TaskBarAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        mAudioView = (AudioView) view;
    }

    @Override
    public void setMediaTask(List<String> tasks) {
        setMediaTask(tasks, false);
    }

    @Override
    public void setMediaTask(List<String> tasks, boolean isAppend) {
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
