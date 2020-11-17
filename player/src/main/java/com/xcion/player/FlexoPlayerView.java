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

import com.xcion.player.audio.AudioTracker;
import com.xcion.player.audio.MCDecoderAudio;
import com.xcion.player.pojo.MediaTask;

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

public class FlexoPlayerView extends FrameLayout implements MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private int mRenderMode;
    private int mCodecMode;
    private int mDisplayMode;
    private int mCoverRes;
    private boolean mVoiceUsable;
    private int mLoadingViewRes;
    private int mMediaControllerViewRes;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private FrameLayout.LayoutParams mMediaParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    private MediaPlayer mMediaPlayer;
    private AudioTracker mAudioTracker;
    MCDecoderAudio mMCDecoderAudio;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private TextureView mTextureView;
    private View mLoadingView;
    private View mMediaControllerView;
    private List<MediaTask> mMediaTasks = new ArrayList<>();
    private int index = 0;

    public enum RenderMode {
        SURFACE_VIEW,
        TEXTURE_VIEW
    }

    public enum CodecMode {
        SOFTWARE_DECODE,
        HARDWARE_DECODE
    }

    public enum DisplayMode {
        FIT_PARENT,
        ORIGIN
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
            mRenderMode = a.getInt(R.styleable.FlexoPlayerView_fpv_render_mode, RenderMode.SURFACE_VIEW.ordinal());
            mCodecMode = a.getInt(R.styleable.FlexoPlayerView_fpv_codec_mode, CodecMode.HARDWARE_DECODE.ordinal());
            mDisplayMode = a.getInt(R.styleable.FlexoPlayerView_fpv_display_mode, DisplayMode.FIT_PARENT.ordinal());
            mVoiceUsable = a.getBoolean(R.styleable.FlexoPlayerView_fpv_voice_usable, true);
            mCoverRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_cover_res, R.drawable.default_video_cover);
            mLoadingViewRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_loading_view_res, R.layout.fpv_loading_view);
            mMediaControllerViewRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_media_controller_view_res, R.layout.fpv_media_controller_view);
        } finally {
            a.recycle();
        }

        initView();
    }

    private void initView() {

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);

        mAudioTracker = new AudioTracker();
        mMCDecoderAudio=new MCDecoderAudio();

        if (RenderMode.TEXTURE_VIEW.ordinal() == mRenderMode) {
            mTextureView = new TextureView(getContext());
            mTextureView.setSurfaceTextureListener(new SurfaceTextureListener());
            this.addView(mTextureView, mMediaParams);
            this.setLayerType(LAYER_TYPE_HARDWARE, null);
        } else {
            mSurfaceView = new SurfaceView(getContext());
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(new SurfaceViewCallback());
            this.addView(mSurfaceView, mMediaParams);
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
            Log.e("sos", "prepare>>>" + mMediaTasks.get(index).getVideoUri());
            //mMediaPlayer.setDataSource(mMediaTasks.get(index).getVideoUri());
            mMediaPlayer.setDataSource("https://vt1.doubanio.com/202011172045/caadc1faf4fdba3dba994f132e2e1faf/view/movie/M/402670836.mp4");
            mMediaPlayer.prepare();
//            mMediaPlayer.setDataSource("https://webfs.yun.kugou.com/202011172057/6aa1738fff7da4fe46d1b06dd843c63e/G240/M04/13/03/MA4DAF-uTEmAXXGeAEd_1k4NwYo400.mp3");
//            mMediaPlayer.prepare();

            mMCDecoderAudio.decodeAudio("https://webfs.yun.kugou.com/202011172057/6aa1738fff7da4fe46d1b06dd843c63e/G240/M04/13/03/MA4DAF-uTEmAXXGeAEd_1k4NwYo400.mp3");
            //mMCDecoderAudio.
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
