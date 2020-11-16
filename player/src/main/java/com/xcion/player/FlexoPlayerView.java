package com.xcion.player;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import com.xcion.player.pojo.MediaTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/16/20 8:03 PM
 * describe: This is...
 */

class FlexoPlayerView extends FrameLayout {

    private int mRenderMode;
    private int mCodecMode;
    private int mDisplayMode;
    private int mCoverRes;
    private boolean mVoiceUsable;
    private int mLoadingViewRes;
    private int mMediaControllerViewRes;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private TextureView mTextureView;
    private View mLoadingView;
    private View mMediaControllerView;
    private List<MediaTask> mediaTasks = new ArrayList<>();

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





    }

}
