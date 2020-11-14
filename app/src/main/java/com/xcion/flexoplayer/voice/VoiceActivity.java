package com.xcion.flexoplayer.voice;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xcion.flexoplayer.R;
import com.xcion.player.audio.AudioLeader;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/11  23:14
 * Intro:
 */
public class VoiceActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TextView mVoiceProgress;
    private TextView mVoiceDuration;
    private SeekBar mVoiceSeekBar;
    private TextView mVoicePlay;
    private TextView mVoicePause;
    private TextView mVoicePrevious;
    private TextView mVoiceNext;

    private ArrayList<String> voices = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        mVoiceProgress = (TextView) findViewById(R.id.voice_progress);
        mVoiceDuration = (TextView) findViewById(R.id.voice_duration);
        mVoiceSeekBar = (SeekBar) findViewById(R.id.voice_seek_bar);
        mVoicePlay = (TextView) findViewById(R.id.voice_play);
        mVoicePause = (TextView) findViewById(R.id.voice_pause);
        mVoicePrevious = (TextView) findViewById(R.id.voice_previous);
        mVoiceNext = (TextView) findViewById(R.id.voice_next);

        mVoicePlay.setOnClickListener(this);
        mVoicePause.setOnClickListener(this);
        mVoicePrevious.setOnClickListener(this);
        mVoiceNext.setOnClickListener(this);
        mVoiceSeekBar.setOnSeekBarChangeListener(this);

        voices.add("https://mallcos.heli33.com/xcion/The%20Giver%20(Reprise).mp3");
        voices.add("https://mallcos.heli33.com/xcion/Win%20You%20Over.mp3");

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voice_play:

                AudioLeader
                        .getInstance(this)
                        .setDataSource(voices);

                break;
            case R.id.voice_pause:


                break;
            case R.id.voice_previous:


                break;
            case R.id.voice_next:


                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
