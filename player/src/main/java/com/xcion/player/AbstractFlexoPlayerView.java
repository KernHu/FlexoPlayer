package com.xcion.player;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.xcion.player.enum1.CodecMode;
import com.xcion.player.enum1.DisplayMode;
import com.xcion.player.enum1.RenderMode;
import com.xcion.player.enum1.Template;
import com.xcion.player.media.Lifecycle;
import com.xcion.player.pojo.MediaTask;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author: Kern Hu
 * email: sky580@126.com
 * data_time: 11/22/20 3:43 PM
 * describe: This is...
 */

abstract class AbstractFlexoPlayerView extends FrameLayout implements Lifecycle<ArrayList<MediaTask>>, GestureDetector.OnGestureListener {

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
    private int controllerViewRes;
    private int taskBarViewRes;

    private GestureDetector mGestureDetector;
    private boolean isShowUI;

    public AbstractFlexoPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public AbstractFlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractFlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbstractFlexoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //设置不熄屏
        if (getContext() instanceof Activity)
            ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //背景设置
        this.setBackgroundColor(getContext().getResources().getColor(R.color.flexo_player_view_background));

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FlexoPlayerView);
        try {
            template = a.getInt(R.styleable.FlexoPlayerView_fpv_template, Template.JUST_STREAM.ordinal());
            renderMode = a.getInt(R.styleable.FlexoPlayerView_fpv_render_mode, RenderMode.SURFACE_VIEW.ordinal());
            codecMode = a.getInt(R.styleable.FlexoPlayerView_fpv_codec_mode, CodecMode.HARDWARE.ordinal());
            displayMode = a.getInt(R.styleable.FlexoPlayerView_fpv_display_mode, DisplayMode.FIT_PARENT.ordinal());
            coverRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_cover_res, R.drawable.default_video_cover);
            loadingViewRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_loading_view_res, R.layout.fpv_loading_view);
            controllerViewRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_controller_view_res, R.layout.fpv_controller_view);
            taskBarViewRes = a.getResourceId(R.styleable.FlexoPlayerView_fpv_taskbar_view_res, R.layout.fpv_taskbar_view);
        } finally {
            a.recycle();
        }


        mGestureDetector = new GestureDetector(getContext(), this);
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

    public void setLoadingViewRes(int loadingViewRes) {
        this.loadingViewRes = loadingViewRes;
    }

    public void setControllerViewRes(int controllerViewRes) {
        this.controllerViewRes = controllerViewRes;
    }

    public int getTemplate() {
        return template;
    }

    public int getDisplayOrientation() {
        return displayOrientation;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isMirror() {
        return mirror;
    }

    public boolean isPreCache() {
        return preCache;
    }

    public int getRenderMode() {
        return renderMode;
    }

    public int getCodecMode() {
        return codecMode;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public int getLoadingViewRes() {
        return loadingViewRes;
    }

    public int getControllerViewRes() {
        return controllerViewRes;
    }

    public int getTaskBarViewRes() {
        return taskBarViewRes;
    }

    protected abstract void initView();

    protected abstract void onBindData();

    protected abstract void onCreateController();

    protected abstract void onCreateTaskBar();

    protected abstract void onCreateCache();

    protected abstract void onCreateStream();

    protected abstract void onCreateAudio();

    protected abstract void onCreateVideo();

    protected abstract void onCreateLive();

    protected abstract void onTaskBarInAnim();

    protected abstract void onTaskBarOutAnim();

    protected abstract void onControllerInAnim();

    protected abstract void onControllerOutAnim();

    protected abstract void onShowUi(boolean isShowUI);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        isShowUI = !isShowUI;
        onShowUi(isShowUI);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
