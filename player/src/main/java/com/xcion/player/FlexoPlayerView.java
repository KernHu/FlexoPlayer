package com.xcion.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.FrameLayout;

import com.xcion.player.audio.AudioPlayerFactory;
import com.xcion.player.controller.ControllerFactory;
import com.xcion.player.pojo.MediaTask;
import com.xcion.player.stream.StreamFactory;
import com.xcion.player.taskbar.TaskBarFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/16/20 8:03 PM
 * describe: This is...
 */
public class FlexoPlayerView extends AbstractFlexoPlayerView implements MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        TextureView.SurfaceTextureListener, SurfaceHolder.Callback {

    private static final String TAG = "FlexoPlayerView";
    private FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private ArrayList<MediaTask> mMediaTasks = new ArrayList<>();
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private TextureView mTextureView;

    private ControllerFactory mControllerFactory;
    private TaskBarFactory mTaskBarFactory;
    private StreamFactory mStreamFactory;
    private AudioPlayerFactory mAudioPlayerFactory;

    private int index = 0;

    public FlexoPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public FlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /*****************************************************************************************/
    /*****************************************************************************************/
    /*****************************************************************************************/
    @Override
    protected void onCreateController() {
        mControllerFactory = new ControllerFactory(getContext(), getControllerViewRes());
        this.addView(mControllerFactory.getView());
    }

    @Override
    protected void onCreateTaskBar() {
        mTaskBarFactory = new TaskBarFactory(getContext(), getTaskBarViewRes());
        this.addView(mTaskBarFactory.getView());
    }


    @Override
    protected void onCreateCache() {

    }

    @Override
    protected void onCreateStream() {
        mStreamFactory = new StreamFactory(getContext());
        this.addView(mStreamFactory.getView());
    }

    @Override
    protected void onCreateAudio() {

        mAudioPlayerFactory = new AudioPlayerFactory(getContext());
        this.addView(mAudioPlayerFactory.getView());
    }

    @Override
    protected void onCreateVideo() {

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);

        if (RenderMode.TEXTURE_VIEW.ordinal() == getRenderMode()) {
            mTextureView = new TextureView(getContext());
            mTextureView.setSurfaceTextureListener(this);
            this.addView(mTextureView, mParams);
            this.setLayerType(LAYER_TYPE_HARDWARE, null);
        } else {
            mSurfaceView = new SurfaceView(getContext());
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
            this.addView(mSurfaceView, mParams);
        }

    }

    @Override
    protected void onCreateLive() {

    }
    /*****************************************************************************************/
    /*****************************************************************************************/
    /*****************************************************************************************/

    @Override
    protected void initView() {

        if (Template.JUST_STREAM.ordinal() == getTemplate()) {
            onCreateStream();
        } else if (Template.JUST_VIDEO.ordinal() == getTemplate()) {
            onCreateVideo();
        } else if (Template.BOTH_AUDIO_STREAM.ordinal() == getTemplate()) {
            onCreateStream();
            onCreateAudio();
        } else if (Template.BOTH_AUDIO_VIDEO.ordinal() == getTemplate()) {
            onCreateAudio();
            onCreateVideo();
        } else {
            onCreateStream();
            onCreateAudio();
            onCreateVideo();
        }
        onCreateTaskBar();
        onCreateController();
    }

    @Override
    public void onBindData() {

    }


    @Override
    public void trimMemory(int level) {

    }

    @Override
    public void recycle() {

    }

    /*****************************************************************************************/
    /*****************************************************************************************/
    /*****************************************************************************************/

    @Override
    public void setMediaTask(List<MediaTask> tasks) {
        setMediaTask(tasks, false);
    }

    @Override
    public void setMediaTask(List<MediaTask> tasks, boolean isAppend) {
        if (tasks != null && !tasks.isEmpty()) {
            if (!isAppend) {
                mMediaTasks.clear();
            }
            mMediaTasks.addAll(tasks);
        }
    }

    @Override
    public void startPlay() {
        mExecutorService.execute(new PlayerRunnable());
    }

    @Override
    public void stopPlay() {

    }

    @Override
    public void lowMemory() {

    }

    /*****************************************************************************************/
    /*****************************************************************************************/
    /*****************************************************************************************/
    class PlayerRunnable implements Runnable {

        @Override
        public void run() {
            prepare(index);
        }
    }

    private void prepare(int index) {

        //任务列表
        if (mTaskBarFactory != null) {
            mTaskBarFactory.setUpdate(mMediaTasks);
        }

        //信息流播放
        if (mStreamFactory != null) {
            mStreamFactory.setStreamTask(mMediaTasks.get(index).getStreamTasks(), false);
        }

        //
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.setDataSource(mMediaTasks.get(index).getVideoUri());
                mMediaPlayer.prepare();
            }
            if (mAudioPlayerFactory != null) {
                mAudioPlayerFactory.setMediaTask(mMediaTasks.get(index).getAudioUrls(), false);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("sos", "prepareMedia>>" + e.toString());
        }
    }

    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared>>" + mediaPlayer.isPlaying());
        mMediaPlayer.start();
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
    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/
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

    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/

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
        mMediaPlayer.stop();
        mMediaPlayer.release();
        Log.d(TAG, "SurfaceTexture即将被销毁>>>");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture surfaceTexture) {
        // SurfaceTexture通过updateImage更新
        Log.d(TAG, "SurfaceTexture通过updateImage更新>>>");
    }
    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/
}
