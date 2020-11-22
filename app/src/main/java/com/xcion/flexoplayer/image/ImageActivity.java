package com.xcion.flexoplayer.image;

import android.os.Bundle;

import com.xcion.flexoplayer.DataUtils;
import com.xcion.flexoplayer.R;
import com.xcion.player.FlexoPlayerView;
import com.xcion.player.pojo.MediaTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/11  23:14
 * Intro:
 */
public class ImageActivity extends AppCompatActivity {

    FlexoPlayerView mFlexoPlayerView;
    private List<MediaTask> mMediaTasks = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mFlexoPlayerView = findViewById(R.id.flexo_player_view);
        mFlexoPlayerView.setMediaTask(DataUtils.getTask());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFlexoPlayerView.startPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFlexoPlayerView.stopPlay();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFlexoPlayerView.recycle();
    }
}
