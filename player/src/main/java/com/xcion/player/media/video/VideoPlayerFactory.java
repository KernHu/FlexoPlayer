package com.xcion.player.media.video;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import com.xcion.player.enum1.RenderMode;
import com.xcion.player.media.AbstractFactory;
import com.xcion.player.media.Lifecycle;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/22/20 4:59 PM
 * describe: This is...
 */

public class VideoPlayerFactory extends AbstractFactory implements Lifecycle<String>, TextureView.SurfaceTextureListener,
        SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private static final String TAG = "VideoPlayerFactory";
    private Context context;
    private int renderMode;

    private FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT);
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private TextureView mTextureView;

    public VideoPlayerFactory(Context context, int renderMode) {
        this.context = context;
        this.renderMode = renderMode;
    }

    @Override
    public View getView() {

        if (RenderMode.TEXTURE_VIEW.ordinal() == renderMode) {
            mTextureView = new TextureView(context);
            mTextureView.setLayoutParams(mParams);
            mTextureView.setSurfaceTextureListener(this);
            return mTextureView;
        } else {
            mSurfaceView = new SurfaceView(context);
            mSurfaceView.setLayoutParams(mParams);
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
            return mSurfaceView;
        }
    }

    public MediaPlayer getMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        return mMediaPlayer;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void setMediaTask(String task) {
        setMediaTask(task, false);
    }

    @Override
    public void setMediaTask(String task, boolean isAppend) {
        mExecutorService.execute(new PlayerRunnable(task));
    }

    class PlayerRunnable implements Runnable {

        private String task;

        public PlayerRunnable(String task) {
            this.task = task;
        }

        @Override
        public void run() {
            try {
                mMediaPlayer.setDataSource(task);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("sos", "IOException>>>>>>" + e.toString());
            }
        }
    }


    @Override
    public void startPlay() {
        if (mMediaPlayer != null) {
            if (!mMediaPlayer.isPlaying())
                mMediaPlayer.start();
        }
    }

    @Override
    public void stopPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void lowMemory() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void trimMemory(int level) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void recycle() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    /***********************************************************************************************/
    /***********************************************************************************************/
    /***********************************************************************************************/
    /**
     * SurfaceView
     */

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated");
        mMediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceDestroyed");
    }

    /***********************************************************************************************/
    /***********************************************************************************************/
    /***********************************************************************************************/

    /**
     * TextureView
     */
    @Override
    public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture surfaceTexture, int i, int i1) {
        Log.d(TAG, "SurfaceTexture准备就绪>>>" + i);
        // SurfaceTexture准备就绪
        mMediaPlayer.setSurface(new Surface(surfaceTexture));
        //mMediaPlayer.prepareAsync();
    }

    @Override
    public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture surfaceTexture, int i, int i1) {
        // SurfaceTexture缓冲大小变化
        Log.d(TAG, "SurfaceTexture缓冲大小变化>>>" + i);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture surfaceTexture) {
        // SurfaceTexture即将被销毁
        Log.d(TAG, "SurfaceTexture即将被销毁>>>");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture surfaceTexture) {
        // SurfaceTexture通过updateImage更新
        Log.d(TAG, "SurfaceTexture通过updateImage更新>>>");
    }
    /***********************************************************************************************/
    /***********************************************************************************************/
    /***********************************************************************************************/

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared>>" + mediaPlayer.isPlaying());
        if (mMediaPlayer != null) {
            if (!mMediaPlayer.isPlaying())
                mMediaPlayer.start();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
        Log.d(TAG, "onVideoSizeChanged>>" + i + "----" + i1);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.d(TAG, "onBufferingUpdate>>" + i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e("sos", "onCompletion>>");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.d(TAG, "onError>>" + i);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        Log.d(TAG, "onInfo>>" + i + "---" + i1);
        return false;
    }
    /***********************************************************************************************/
    /***********************************************************************************************/
    /***********************************************************************************************/
}
