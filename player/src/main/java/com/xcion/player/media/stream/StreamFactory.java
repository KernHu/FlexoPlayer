package com.xcion.player.media.stream;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.xcion.player.media.AbstractFactory;
import com.xcion.player.media.Lifecycle;
import com.xcion.player.pojo.StreamTask;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/22/20 4:59 PM
 * describe: This is...
 */

public class StreamFactory extends AbstractFactory implements Lifecycle<StreamTask> {

    private Context context;

    private StreamPlayerView mStreamPlayerView;

    public StreamFactory(Context context) {
        this.context = context;
    }

    @Override
    public View getView() {

        FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        StreamPlayerView mStreamPlayerView = new StreamPlayerView(context);
        mStreamPlayerView
                .setScrolling(StreamPlayerView.Scrolling.SCROLLING_HORIZONTAL.ordinal())
                .setDelayed(5)
                .setSmoothRate(1F)
                .setStreamTask(null, false)
                .build();
        mStreamPlayerView.setLayoutParams(mParams);

        initView(mStreamPlayerView);

        return mStreamPlayerView;
    }

    @Override
    public void initView(View view) {

        mStreamPlayerView = (StreamPlayerView) view;
    }


    @Override
    public void setMediaTask(ArrayList<StreamTask> tasks) {
            setMediaTask(tasks, false);
    }

    @Override
    public void setMediaTask(ArrayList<StreamTask> tasks, boolean isAppend) {
        if (mStreamPlayerView != null) {
            mStreamPlayerView.setStreamTask(tasks, isAppend).build();
            mStreamPlayerView.startPlay();
        }
    }

    @Override
    public void startPlay() {

    }

    @Override
    public void stopPlay() {

    }

    @Override
    public void lowMemory() {

    }

    @Override
    public void trimMemory(int level) {

    }

    @Override
    public void recycle() {

    }
}
