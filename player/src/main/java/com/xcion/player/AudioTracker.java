package com.xcion.player;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;

import com.xcion.player.audio.AudioError;
import com.xcion.player.audio.AudioListener;

import java.io.IOException;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/13  23:01
 * Intro:
 */
public class AudioTracker {

    private static final int SAMPLE_RATE_IN_HZ = 44100;
    private MediaExtractor mMediaExtractor;
    private MediaCodec mMediaCodec;
    private AudioTrack mAudioTrack;
    private AudioListener mAudioListener;

    public AudioTracker(AudioListener audioListener) {
        this.mAudioListener = audioListener;
        //初始化解码器
        mMediaExtractor = new MediaExtractor();

        //初始化播放器
        int minBufferSize = AudioTrack
                .getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, Math.max(minBufferSize, 2048), AudioTrack.MODE_STREAM);

    }

    public void setDecode(String sourceUrl) {
        String mime = null;
        int trackIndex = -1;
        MediaFormat trackFormat = null;
        try {
            mMediaExtractor.setDataSource(sourceUrl);
            for (int i = 0; i < mMediaExtractor.getTrackCount(); i++) {
                trackFormat = mMediaExtractor.getTrackFormat(i);
                mime = trackFormat.getString(MediaFormat.KEY_MIME);
                if (!TextUtils.isEmpty(mime) && mime.startsWith("audio")) {
                    trackIndex = i;
                    break;
                }
            }
            if (trackIndex == -1) {
                if(mAudioListener!=null){
                    mAudioListener.onFailure(new AudioError(10001,"没有找到音频流"));
                }
                return;
            }
            //选择此音轨
            mMediaExtractor.selectTrack(trackIndex);
            //创建解码器
            mMediaCodec = MediaCodec.createDecoderByType(mime);
            mMediaCodec.configure(trackFormat, null, null, 0);
            mMediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
            if(mAudioListener!=null){
                mAudioListener.onFailure(new AudioError(10002,"没有找到音频流"));
            }
        }

    }
}
