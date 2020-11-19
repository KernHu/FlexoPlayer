package com.xcion.player;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import com.xcion.player.audio.MCDecoderAudio;
import com.xcion.player.pojo.MediaTask;
import com.xcion.player.stream.StreamPlayerView;

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

public class FlexoPlayerView extends FrameLayout implements FlexoPlayerLifecycle, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private int template;
    private int displayOrientation;
    private float volume;
    private boolean mirror;
    private boolean preCache;
    private int renderMode;
    private int codecMode;
    private int displayMode;
    private int coverRes;
    private int loadingViewRes;
    private int mediaControllerViewRes;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    private MediaPlayer mMediaPlayer;
    private MCDecoderAudio mMCDecoderAudio;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private TextureView mTextureView;
    private StreamPlayerView mStreamPlayerView;
    private View mLoadingView;
    private View mMediaControllerView;
    private List<MediaTask> mMediaTasks = new ArrayList<>();
    private int index = 0;

    public enum Template {
        JUST_STREAM,
        JUST_VIDEO,
        BOTH_AUDIO_STREAM,
        BOTH_AUDIO_VIDEO,
        ALL
    }

    public enum RenderMode {
        SURFACE_VIEW,
        TEXTURE_VIEW
    }

    public enum CodecMode {
        SOFTWARE,
        HARDWARE
    }

    public enum DisplayMode {
        FIT_PARENT,
        ORIGIN
    }

    public void setTemplate(Template template) {
        this.template = template.ordinal();
    }

    public void setDisplayOrientation(DisplayMode displayOrientation) {
        this.displayOrientation = displayOrientation.ordinal();
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public void setPreCache(boolean preCache) {
        this.preCache = preCache;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode.ordinal();
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode.ordinal();
    }

    public int getCoverRes() {
        return coverRes;
    }

    public void setCoverRes(int coverRes) {
        this.coverRes = coverRes;
    }

    public int getLoadingViewRes() {
        return loadingViewRes;
    }

    public void setLoadingViewRes(int loadingViewRes) {
        this.loadingViewRes = loadingViewRes;
    }

    public int getMediaControllerViewRes() {
        return mediaControllerViewRes;
    }

    public void setMediaControllerViewRes(int mediaControllerViewRes) {
        this.mediaControllerViewRes = mediaControllerViewRes;
    }

    public FlexoPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public FlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setBackgroundColor(getContext().getResources().getColor(R.color.flexo_player_view_background));

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FlexoPlayerView);
        try {
            template = a.getInt(R.styleable.FlexoPlayerView_fpv_template, Template.ALL.ordinal());
            renderMode = a.getInt(R.styleable.FlexoPlayerView_fpv_render_mode, RenderMode.SURFACE_VIEW.ordinal());
            codecMode = a.getInt(R.styleable.FlexoPlayerView_fpv_codec_mode, CodecMode.HARDWARE.ordinal());
            displayMode = a.getInt(R.styleable.FlexoPlayerView_fpv_display_mode, DisplayMode.FIT_PARENT.ordinal());
            coverRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_cover_res, R.drawable.default_video_cover);
            loadingViewRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_loading_view_res, R.layout.fpv_loading_view);
            mediaControllerViewRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_media_controller_view_res, R.layout.fpv_media_controller_view);
        } finally {
            a.recycle();
        }

        initView();
    }

    @Override
    public void onCreateStream() {

        mStreamPlayerView = new StreamPlayerView(getContext());
        mStreamPlayerView
                .setScrolling(StreamPlayerView.Scrolling.SCROLLING_HORIZONTAL.ordinal())
                .setDelayed(5)
                .setSmoothRate(1F)
                .setStreamTask(null, false)
                .build();
        this.addView(mStreamPlayerView, mParams);

    }

    @Override
    public void onCreateAudio() {

        mMCDecoderAudio = new MCDecoderAudio();

    }

    @Override
    public void onCreateVideo() {

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);

        if (RenderMode.TEXTURE_VIEW.ordinal() == renderMode) {
            mTextureView = new TextureView(getContext());
            mTextureView.setSurfaceTextureListener(new SurfaceTextureListener());
            this.addView(mTextureView, mParams);
            this.setLayerType(LAYER_TYPE_HARDWARE, null);
        } else {
            mSurfaceView = new SurfaceView(getContext());
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(new SurfaceViewCallback());
            this.addView(mSurfaceView, mParams);
        }

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onBindData() {

    }

    @Override
    public void onResume() {
        startPlay();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onDestroy() {

    }


    private void initView() {

        if (Template.JUST_STREAM.ordinal() == template) {
            onCreateStream();
        } else if (Template.JUST_VIDEO.ordinal() == template) {
            onCreateVideo();
        } else if (Template.BOTH_AUDIO_STREAM.ordinal() == template) {
            onCreateStream();
            onCreateAudio();
        } else if (Template.BOTH_AUDIO_VIDEO.ordinal() == template) {
            onCreateAudio();
            onCreateVideo();
        } else {
            onCreateStream();
            onCreateAudio();
            onCreateVideo();
        }

    }

    public void setMediaTask(List<MediaTask> mediaTasks) {
        setMediaTask(mediaTasks, false);
    }

    public void setMediaTask(List<MediaTask> mediaTasks, boolean isAppend) {
        if (mediaTasks != null && !mediaTasks.isEmpty()) {
            if (!isAppend) {
                mMediaTasks.clear();
            }
            mMediaTasks.addAll(mediaTasks);
        }
    }

    public void startPlay() {
        mExecutorService.execute(new PlayerRunnable());
    }

    class PlayerRunnable implements Runnable {

        @Override
        public void run() {
            prepare(index);
        }
    }

    private void prepare(int index) {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.setDataSource(mMediaTasks.get(index).getVideoUri());
                mMediaPlayer.prepare();
            }
            if (mMCDecoderAudio != null) {
                mMCDecoderAudio.decodeAudio(mMediaTasks.get(index).getAudioUrls().get(0));
            }
            if (mStreamPlayerView != null) {
                mStreamPlayerView.setStreamTask(mMediaTasks.get(index).getStreamTasks(), false).build();
                mStreamPlayerView.startPostDelayed();
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
        Log.e("sos", "onPrepared>>" + mediaPlayer.isPlaying());
        mMediaPlayer.start();
        //mMediaPlayer.setLooping(true);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e("sos", "onVideoSizeChanged>>" + i + "----" + i1);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.e("sos", "onBufferingUpdate>>" + i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e("sos", "onCompletion>>");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e("sos", "onError>>" + i);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e("sos", "onInfo>>" + i + "---" + i1);
        return false;
    }
    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/
    /**
     * SurfaceView
     */
    public class SurfaceViewCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.e("sos", "surfaceCreated");
            mMediaPlayer.setDisplay(surfaceHolder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            Log.e("sos", "surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.e("sos", "surfaceDestroyed");
        }
    }
    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/

    /**
     * TextureView
     */
    class SurfaceTextureListener implements TextureView.SurfaceTextureListener {

        @Override
        public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture surfaceTexture, int i, int i1) {
            Log.e("sos", "SurfaceTexture准备就绪>>>" + i);
            // SurfaceTexture准备就绪
            mMediaPlayer.setSurface(new Surface(surfaceTexture));
            //mMediaPlayer.prepareAsync();
        }

        @Override
        public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture surfaceTexture, int i, int i1) {
            // SurfaceTexture缓冲大小变化
            Log.e("sos", "SurfaceTexture缓冲大小变化>>>" + i);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture surfaceTexture) {
            // SurfaceTexture即将被销毁
            mMediaPlayer.stop();
            mMediaPlayer.release();
            Log.e("sos", "SurfaceTexture即将被销毁>>>");
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture surfaceTexture) {
            // SurfaceTexture通过updateImage更新
            Log.e("sos", "SurfaceTexture通过updateImage更新>>>");
        }
    }
    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/
}
