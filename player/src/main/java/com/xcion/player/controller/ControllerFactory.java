package com.xcion.player.controller;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xcion.player.AbstractFactory;
import com.xcion.player.R;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/22/20 4:59 PM
 * describe: This is...
 */

public class ControllerFactory extends AbstractFactory {

    private Context context;
    private int controllerViewRes;

    private ImageView mFastRewind;
    private ImageView mPlayerState;
    private ImageView mFastForward;
    private TextView mPlayerProgress;
    private TextView mPlayerDuration;
    private SeekBar mSeekBar;

    public ControllerFactory(Context context, int controllerViewRes) {
        this.context = context;
        this.controllerViewRes = controllerViewRes;
    }

    @Override
    public View getView() {

        View view = ViewGroup.inflate(context, controllerViewRes, null);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        view.setLayoutParams(lp);
        initView(view);

        return view;
    }

    @Override
    public void initView(View view) {

        mFastRewind = view.findViewById(R.id.flexo_player_fast_rewind);
        mPlayerState = view.findViewById(R.id.flexo_player_state);
        mFastForward = view.findViewById(R.id.flexo_player_fast_forward);
        mPlayerProgress = view.findViewById(R.id.flexo_player_progress);
        mPlayerDuration = view.findViewById(R.id.flexo_player_duration);
        mSeekBar = view.findViewById(R.id.flexo_player_seekbar);

    }
}
