package com.xcion.flexoplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.xcion.player.FlexoPlayerView;
import com.xcion.player.pojo.MediaTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/11  23:13
 * Intro:
 */
public class VideoActivity extends AppCompatActivity {

    FlexoPlayerView mFlexoPlayerView;
    private List<MediaTask> mMediaTasks = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mFlexoPlayerView = findViewById(R.id.flexo_player_view);

        ArrayList<String> voices = new ArrayList<>();
        voices.add("https://mallcos.heli33.com/xcion/The%20Giver%20(Reprise).mp3");
        voices.add("https://mallcos.heli33.com/xcion/Win%20You%20Over.mp3");
        ArrayList<String> images = new ArrayList<>();
        images.add("https://mallcos.heli33.com/xcion/u%3D208198998%2C2434358897%26fm%3D15%26gp%3D0.jpg");
        images.add("https://heli-mall-bucket-1300605706.cos.ap-shanghai.myqcloud.com/xcion/u%3D2337363485%2C485295659%26fm%3D26%26gp%3D0.jpg");
        images.add("https://mallcos.heli33.com/xcion/u%3D3111234716%2C3377315879%26fm%3D26%26gp%3D0.jpg");
        images.add("https://mallcos.heli33.com/xcion/u%3D3391605298%2C1122672461%26fm%3D15%26gp%3D0.jpg");
        ArrayList<String> messages = new ArrayList<>();
        voices.add("来，跟著我来，拿一面白旗在你们的手里??不是上面写著激动怨毒，鼓励残杀字样的白旗，也不是涂著不洁净血液的标记的白旗，也不是画著忏悔与咒语的白旗(把忏悔画在你们的心里)；");
        voices.add("悄悄是别离的笙箫，夏虫也为我沉默，沉默是今晚的康桥。——徐志摩《再别康桥》");
        mMediaTasks.add(new MediaTask("https://mallcos.heli33.com/xcion/201706092014.mp4", voices, images, messages));
        mFlexoPlayerView.setMediaTask(mMediaTasks);

        play();
    }

    public void play() {

        mFlexoPlayerView.startPlay();
    }
}
