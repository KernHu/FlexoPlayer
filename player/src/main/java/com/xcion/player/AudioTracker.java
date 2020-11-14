package com.xcion.player;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.util.Log;

import com.xcion.player.audio.AudioError;
import com.xcion.player.audio.AudioListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Kern
 * E-mail: sky580@126.com
 * DateTime: 2020/11/13  23:01
 * Intro:
 */
public class AudioTracker {

    private static final int SAMPLE_RATE_IN_HZ = 44100;
    private static final long TIME_OUT_US = 10 * 1000;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private MediaExtractor mMediaExtractor;
    private MediaCodec mMediaCodec;
    private AudioTrack mAudioTrack;
    private AudioListener mAudioListener;
    private String sourceUrl;
    private boolean completed = false;

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void play() {
        mExecutorService.execute(new TaskRunnable());
    }

    public AudioTracker(AudioListener audioListener) {
        this.mAudioListener = audioListener;
    }

    private void initConfig() {
        //初始化解码器
        mMediaExtractor = new MediaExtractor();

        //初始化播放器
        int minBufferSize = AudioTrack
                .getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, Math.max(minBufferSize, 2048), AudioTrack.MODE_STREAM);
        mAudioTrack.play();
    }

    /**
     * 解码音频资源
     *
     * @param
     */
    private void startMediaCodec() {

        if (TextUtils.isEmpty(sourceUrl)) {
            if (mAudioListener != null) {
                mAudioListener.onFailure(AudioError.NOT_FOUND_AUDIO_STREAM);
            }
            return;
        }

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
                if (mAudioListener != null) {
                    mAudioListener.onFailure(AudioError.NOT_FOUND_AUDIO_STREAM);
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
            if (mAudioListener != null) {
                mAudioListener.onFailure(AudioError.DATA_SOURCE_ERROR);
            }
        }
    }

    /**
     * 编解码边播放
     */
    private void decodePlay() {
        OutputStream os = null;
        if (!TextUtils.isEmpty(sourceUrl)) {
            try {
                os = new FileOutputStream(sourceUrl);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                if (mAudioListener != null) {
                    mAudioListener.onFailure(AudioError.DATA_SOURCE_ERROR);
                }
            }
        } else {
            if (mAudioListener != null) {
                mAudioListener.onFailure(AudioError.NOT_FOUND_AUDIO_STREAM);
            }
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        ByteBuffer inputBuffer;
        while (!completed && mMediaCodec != null) {
            //获取可用的inputBuffer，输入参数-1代表一直等到，0代表不等待，10*1000代表10秒超时
            //超时时间10秒
            int inputIndex = mMediaCodec.dequeueInputBuffer(TIME_OUT_US);
            if (inputIndex < 0) {
                if (mAudioListener != null) {
                    mAudioListener.onFailure(AudioError.DECODE_TIME_OUT);
                }
                break;
            }
            inputBuffer = mMediaCodec.getInputBuffer(inputIndex);
            if (inputBuffer != null) {
                inputBuffer.clear();
            } else {
                continue;
            }
            //从流中读取的采用数据的大小
            int sampleSize = mMediaExtractor.readSampleData(inputBuffer, 0);
            if (sampleSize > 0) {
                //入队解码
                mMediaCodec.queueInputBuffer(inputIndex, 0, sampleSize, 0, 0);
                //移动到下一个采样点
                mMediaExtractor.advance();
            } else {
                if (mAudioListener != null) {
                    mAudioListener.onFailure(AudioError.DECODE_TIME_OUT);
                }
                break;
            }
            //取解码后的数据
            int outputIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIME_OUT_US);
            //不一定能一次取完，所以要循环取
            ByteBuffer outputBuffer;
            byte[] pcmData;
            while (outputIndex >= 0) {
                outputBuffer = mMediaCodec.getOutputBuffer(outputIndex);
                pcmData = new byte[bufferInfo.size];
                if (outputBuffer != null) {
                    outputBuffer.get(pcmData);
                    outputBuffer.clear();//用完后清空，复用
                }
                //播放pcm数据
                mAudioTrack.write(pcmData, 0, bufferInfo.size);
                //写入到本地文件中
                if (os != null) {
                    try {
                        os.write(pcmData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //释放
                mMediaCodec.releaseOutputBuffer(outputIndex, false);
                //再次获取数据
                outputIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIME_OUT_US);
            }
        }
        //释放解码器
        if (mMediaCodec != null) {
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mAudioListener != null) {
            mAudioListener.onComplete();
        }

    }


    class TaskRunnable implements Runnable {

        @Override
        public void run() {
//            initConfig();
//            startMediaCodec();
//            decodePlay();

            initMediaCodec();
            //初始化播放器
            initAudioTrack();

            decodeAndPlay();
        }
    }



    /**
     * 实际的解码工作
     */
    private void decodeAndPlay() {
        OutputStream os = null;
        if (!TextUtils.isEmpty(sourceUrl)){
            try {
                os = new FileOutputStream(sourceUrl);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        ByteBuffer inputBuffer;
        while (!completed && mMediaCodec != null) {
            //获取可用的inputBuffer，输入参数-1代表一直等到，0代表不等待，10*1000代表10秒超时
            //超时时间10秒
            long TIME_OUT_US = 10 * 1000;
            int inputIndex = mMediaCodec.dequeueInputBuffer(TIME_OUT_US);
            if (inputIndex < 0) {
                break;
            }
            inputBuffer = mMediaCodec.getInputBuffer(inputIndex);
            if (inputBuffer != null) {
                inputBuffer.clear();
            }else {
                continue;
            }
            //从流中读取的采用数据的大小
            int sampleSize = mMediaExtractor.readSampleData(inputBuffer, 0);
            if (sampleSize > 0) {
                //入队解码
                mMediaCodec.queueInputBuffer(inputIndex, 0, sampleSize, 0, 0);
                //移动到下一个采样点
                mMediaExtractor.advance();
            } else {
                break;
            }
            //取解码后的数据
            int outputIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIME_OUT_US);
            //不一定能一次取完，所以要循环取
            ByteBuffer outputBuffer;
            byte[] pcmData;
            while (outputIndex >= 0) {
                outputBuffer = mMediaCodec.getOutputBuffer(outputIndex);
                pcmData = new byte[bufferInfo.size];
                if (outputBuffer != null) {
                    outputBuffer.get(pcmData);
                    outputBuffer.clear();//用完后清空，复用
                }
                //播放pcm数据
                mAudioTrack.write(pcmData, 0, bufferInfo.size);
                //写入到本地文件中
                if (os != null) {
                    try {
                        os.write(pcmData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //释放
                mMediaCodec.releaseOutputBuffer(outputIndex, false);
                //再次获取数据
                outputIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIME_OUT_US);
            }
        }
        //释放解码器
        if (mMediaCodec != null) {
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化MediaCodec
     */
    private void initMediaCodec() {
        try {
            mMediaExtractor = new MediaExtractor();
            mMediaExtractor.setDataSource(sourceUrl);
            //找到音频流的索引
            int audioTrackIndex = -1;
            String mime = null;
            MediaFormat trackFormat = null;
            for (int i = 0; i < mMediaExtractor.getTrackCount(); i++) {
                trackFormat = mMediaExtractor.getTrackFormat(i);
                mime = trackFormat.getString(MediaFormat.KEY_MIME);
                if (!TextUtils.isEmpty(mime) && mime.startsWith("audio")) {
                    audioTrackIndex = i;
                    break;
                }
            }
            //没有找到音频流的情况下
            if (audioTrackIndex == -1) {
                return;
            }
            //选择此音轨
            mMediaExtractor.selectTrack(audioTrackIndex);
            //创建解码器
            mMediaCodec = MediaCodec.createDecoderByType(mime);
            mMediaCodec.configure(trackFormat, null, null, 0);
            mMediaCodec.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化播放器
     */
    private void initAudioTrack() {
        int streamType = AudioManager.STREAM_MUSIC;
        int sampleRate = 44100;
        int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int mode = AudioTrack.MODE_STREAM;

        int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);

        mAudioTrack = new AudioTrack(streamType, sampleRate, channelConfig, audioFormat,
                Math.max(minBufferSize, 2048), mode);
        mAudioTrack.play();
    }

}
