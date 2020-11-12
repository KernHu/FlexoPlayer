package com.xcion.flexoplayer.voice;

import android.content.Intent;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xcion.flexoplayer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    private AudioTrack mAudioTrack;
    private int bufSize;

    private int position;
    private List<String> voices = new ArrayList<>();

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

//        MCDecoderAudio decoderAudio = new MCDecoderAudio();
//        decoderAudio.decodeAudio(voices.get(position));


        Intent mIntent = new Intent(VoiceActivity.this, VoicerService.class);
        startActivity(mIntent);

    }

//    private void bindData() {
//
//        // 获取最小缓冲区
//        bufSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
//        // 实例化AudioTrack(设置缓冲区为最小缓冲区的2倍，至少要等于最小缓冲区)
//        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO,
//                AudioFormat.ENCODING_PCM_16BIT, bufSize * 2, AudioTrack.MODE_STREAM);
//        // 设置音量
//        mAudioTrack.setVolume(2f);
//        // 设置播放频率
//        mAudioTrack.setPlaybackRate(10);
//
//        //线程池管理播放
//        mSingleThreadExecutor.execute(new PlayerRunnable(voices.get(1)));
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voice_play:

                if (mAudioTrack != null) {
                    mAudioTrack.play();
                }

                break;
            case R.id.voice_pause:

                if (mAudioTrack != null) {
                    mAudioTrack.pause();
                }

                break;
            case R.id.voice_previous:

//                if (mAudioTrack != null) {
//                    position = position <= 0 ? 0 : position--;
//                    //线程池管理播放
//                    mSingleThreadExecutor.execute(new PlayerRunnable(voices.get(position % voices.size())));
//                }

                break;
            case R.id.voice_next:

//                if (mAudioTrack != null) {
//                    position++;
//                    //线程池管理播放
//                    mSingleThreadExecutor.execute(new PlayerRunnable(voices.get(position % voices.size())));
//                }

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


//    private void jetPlayStream(String voiceUrl) {
//        DataInputStream dis = null;
//        try {
//            // 获取音乐文件输入流
//            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(voiceUrl)));
//            byte[] buffer = new byte[bufSize * 2];
//            int len;
//            while ((len = dis.read(buffer, 0, buffer.length)) != -1) {
//                // 将读取的数据，写入Audiotrack
//                mAudioTrack.write(buffer, 0, buffer.length);
//                Log.e("sos", "获取音乐文件输入流>>>" + len);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("sos", "Exception>>>" + e.getMessage());
//        } finally {
//            if (dis != null) {
//                try {
//                    dis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


//    class PlayerRunnable implements Runnable {
//
//        private String voiceUrl;
//
//        public PlayerRunnable(String voiceUrl) {
//            this.voiceUrl = voiceUrl;
//        }
//
//        @Override
//        public void run() {
//            jetPlayStream(voiceUrl);
//        }
//    }
}
