package com.xcion.player.stream;

import android.content.Context;
import android.util.AttributeSet;

import com.xcion.player.R;
import com.xcion.player.stream.adapter.StreamAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/18/20 8:12 PM
 * describe: This is...
 */

public class StreamPlayerView extends RecyclerView {

    public enum Scrolling {
        SCROLLING_VERTICAL,
        SCROLLING_HORIZONTAL
    }

    private Scrolling scrolling;
    private int smoothRate;
    private int delayed;

    public Scrolling getScrolling() {
        return scrolling;
    }

    public StreamPlayerView setScrolling(Scrolling scrolling) {
        this.scrolling = scrolling;
        return this;
    }

    public int getSmoothRate() {
        return smoothRate;
    }

    public StreamPlayerView setSmoothRate(int smoothRate) {
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

    }
}
