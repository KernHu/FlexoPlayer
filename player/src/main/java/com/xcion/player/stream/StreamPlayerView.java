package com.xcion.player.stream;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.xcion.player.Lifecycle;
import com.xcion.player.R;
import com.xcion.player.pojo.StreamTask;
import com.xcion.player.stream.adapter.StreamAdapter;
import com.xcion.player.widget.SmoothLayoutManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/18/20 8:12 PM
 * describe: This is...
 */

public class StreamPlayerView extends RecyclerView implements Lifecycle, Handler.Callback {

    public enum Scrolling {
        SCROLLING_HORIZONTAL,
        SCROLLING_VERTICAL,
    }

    private Handler mHandler = new Handler(this);
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
    private int scrolling;
    private float smoothRate;
    private int delayed;
    private ArrayList<StreamTask> streamTask = new ArrayList<>();
    private int position;

    public int getScrolling() {
        return scrolling;
    }

    public StreamPlayerView setScrolling(int scrolling) {
        this.scrolling = scrolling;
        return this;
    }

    public float getSmoothRate() {
        return smoothRate;
    }

    public StreamPlayerView setSmoothRate(float smoothRate) {
        this.smoothRate = smoothRate;
        return this;
    }

    public int getDelayed() {
        return delayed;
    }

    public StreamPlayerView setDelayed(int delayed) {
        this.delayed = delayed;
        return this;
    }

    public ArrayList<StreamTask> getStreamTask() {
        return streamTask;
    }

    public StreamPlayerView setStreamTask(ArrayList<StreamTask> streamTask, boolean isAppend) {
        if (streamTask != null) {
            if (!isAppend)
                this.streamTask.clear();
            this.streamTask.addAll(streamTask);
        }
        return this;
    }

    public void build() {
        onBindData();
        onResume();
    }

    /********************************************************************************************************/

    private StreamAdapter mStreamAdapter;

    public StreamPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public StreamPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StreamPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackgroundColor(getContext().getResources().getColor(R.color.flexo_player_view_background));
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StreamPlayerView);
        try {
            scrolling = a.getInt(R.styleable.StreamPlayerView_spv_scrolling, Scrolling.SCROLLING_HORIZONTAL.ordinal());
            smoothRate = a.getFloat(R.styleable.StreamPlayerView_spv_smooth_rate, 10);
            delayed = a.getInt(R.styleable.StreamPlayerView_spv_delayed, 5);
        } finally {
            a.recycle();
        }
        onCreate();
    }

    public void startPostDelayed() {
        Log.e("sos", "startPostDelayed>>>");
        mHandler.postDelayed(runnable, getDelayed() * 1000);
    }

    private void stopPostDelayed() {
        mHandler.removeCallbacks(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(position);
        }
    };

    @Override
    public boolean handleMessage(@NonNull Message message) {
        position++;
        position = (position > mStreamAdapter.getItemCount() - 1) ? 0 : position;
        Log.e("sos", "position>>>" + position);
        this.scrollToPosition(position);
        startPostDelayed();
        return false;
    }

    @Override
    public void onCreate() {

        SmoothLayoutManager manager = new SmoothLayoutManager(getContext(), scrolling, false, smoothRate);
        this.setLayoutManager(manager);

        mStreamAdapter = new StreamAdapter(getContext(), streamTask);
        this.setAdapter(mStreamAdapter);
        mPagerSnapHelper.attachToRecyclerView(this);
    }

    @Override
    public void onBindData() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mStreamAdapter.setUpdate(streamTask);
            }
        });
    }

    @Override
    public void onResume() {
        Log.e("sos", "onResume>>>");
        //startPostDelayed();
    }

    @Override
    public void onPause() {
        stopPostDelayed();
    }

    @Override
    public void onLowMemory() {
        Glide.get(getContext()).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Glide.get(getContext()).onTrimMemory(level);
    }

    @Override
    public void onDestroy() {
        stopPostDelayed();
        Glide.get(getContext()).clearMemory();
        Glide.get(getContext()).clearDiskCache();
        mHandler = null;
        mStreamAdapter = null;
        streamTask.clear();
        streamTask = null;
    }

}
