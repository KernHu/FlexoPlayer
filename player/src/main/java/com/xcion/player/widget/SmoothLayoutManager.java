package com.xcion.player.widget;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 2019/7/28 8:48 PM
 * describe: This is...
 */
public class SmoothLayoutManager extends LinearLayoutManager {

    /**
     * RecyclerView 滑动速率
     */
    public static float SLIDE_RATE = 0.0f;

    public SmoothLayoutManager(Context context) {
        this(context, LinearLayoutManager.VERTICAL, false);
    }

    public SmoothLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SmoothLayoutManager(Context context, int orientation, boolean reverseLayout, float slideRate) {
        super(context, orientation, reverseLayout);
        this.SLIDE_RATE = slideRate;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller mLinearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        //返回0.2 官方注释：计算滚动速度。如果返回值是2毫秒，这表示着滚动1000像素需要2秒。
                        return SLIDE_RATE;
                    }
                };
        mLinearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(mLinearSmoothScroller);
    }
}
