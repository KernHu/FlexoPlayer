package com.xcion.player.media.stream;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.xcion.player.R;
import com.xcion.player.media.Lifecycle;
import com.xcion.player.media.video.VideoPlayerFactory;
import com.xcion.player.pojo.StreamTask;
import com.xcion.player.widget.SmoothLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

public class StreamPlayerView extends RecyclerView implements Lifecycle<ArrayList<StreamTask>>, Handler.Callback {

    public enum Scrolling {
        SCROLLING_HORIZONTAL,
        SCROLLING_VERTICAL,
    }

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private Handler mHandler = new Handler(this);
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
    private int scrolling;
    private float smoothRate;
    private int delayed;
    private ArrayList<StreamTask> streamTask = new ArrayList<>();

    private StreamAdapter mStreamAdapter;
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
    }

    /********************************************************************************************************/
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

        initView();
    }

    private void initView() {

        SmoothLayoutManager manager = new SmoothLayoutManager(getContext(), scrolling, false, smoothRate);
        this.setLayoutManager(manager);

        mStreamAdapter = new StreamAdapter(getContext(), streamTask);
        this.setAdapter(mStreamAdapter);
        mPagerSnapHelper.attachToRecyclerView(this);

    }

    protected void startPostDelayed() {
        mHandler.postDelayed(runnable, getDelayed() * 1000);
    }

    protected void stopPostDelayed() {
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
        this.scrollToPosition(position);
        startPostDelayed();
        return false;
    }


    public void onBindData() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mStreamAdapter.setUpdate(streamTask);
            }
        });
    }

    /**
     * @return
     */
    private long getContentLength() {
        return delayed * streamTask.size();
    }

    /************************************************************************************/
    @Override
    public void setMediaTask(ArrayList<StreamTask> tasks) {
        setMediaTask(tasks, false);
    }

    @Override
    public void setMediaTask(ArrayList<StreamTask> tasks, boolean isAppend) {
        if (streamTask != null) {
            if (!isAppend)
                this.streamTask.clear();
            this.streamTask.addAll(streamTask);
        }
    }

    @Override
    public void startPlay() {
        startPostDelayed();
    }

    @Override
    public void stopPlay() {
        stopPostDelayed();
    }

    @Override
    public void lowMemory() {
        Glide.get(getContext()).onLowMemory();
    }

    @Override
    public void trimMemory(int level) {
        Glide.get(getContext()).onTrimMemory(level);
    }

    @Override
    public void recycle() {
        stopPostDelayed();
        Glide.get(getContext()).clearMemory();
        mExecutorService.execute(new CacheRunnable());
        mHandler = null;
        mStreamAdapter = null;
        streamTask.clear();
        streamTask = null;
    }


    class CacheRunnable implements Runnable {

        @Override
        public void run() {
            Glide.get(getContext()).clearDiskCache();
        }
    }

}
